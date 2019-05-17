package com.example.socketsproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socketsproject.utils.Constants;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity implements Runnable {

    private final String TAG = this.getClass().getSimpleName();

    private EditText nomeDaPessoa;
    private TextView informarConexao;
    private Button conectarButton;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = getApplicationContext();

        nomeDaPessoa = findViewById(R.id.nome_da_pessoa_edt);
        informarConexao = findViewById(R.id.informar_conexao_txt);
        conectarButton = findViewById(R.id.conectar_btn);

        conectarButton.setOnClickListener(buttonClickListener);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Thread(LoginActivity.this).start();
        }
    };

    @Override
    public void run() {
        informarConexao.setTextColor(mContext.getColor(R.color.colorBlack));
        informarConexao.setText("Conectado com o servidor, aguardando...");

        try {
            Socket socket = new Socket(Constants.IPV4, Constants.PORT_NUMBER);
            Log.d(TAG, "run: socket conectado: " + socket.toString());

            new Thread(new ReceptorMensagens(socket.getInputStream())).start();

            Log.d(TAG, "run: printStream");
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            printStream.println(nomeDaPessoa.getText());

            Log.d(TAG, "run: close");
            printStream.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
            informarConexao.setTextColor(mContext.getColor(R.color.colorRed));
            informarConexao.setText("Problema com o servidor, por favor, tente novamente.");
        }
    }
}
