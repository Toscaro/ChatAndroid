package com.example.socketsproject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socketsproject.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity implements Runnable, Serializable {

    private EditText nomeDaPessoa;
    private EditText nomeDeUsuario;
    private TextView informarConexao;
    private Button conectarButton;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = getApplicationContext();

        nomeDaPessoa = findViewById(R.id.nome_da_pessoa_edt);
        nomeDeUsuario = findViewById(R.id.nome_de_usuario_edt);
        informarConexao = findViewById(R.id.informar_conexao_txt);
        conectarButton = findViewById(R.id.conectar_btn);
        

        conectarButton.setOnClickListener(buttonClickListener);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Thread thread = new Thread(LoginActivity.this);
            thread.start();
        }
    };

    @Override
    public void run() {

        try {
            Socket socket = new Socket(Constants.IPV4, Constants.PORT_NUMBER);

            informarConexao.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack));
            informarConexao.setText("Conectado com o servidor, aguardando...");

            String nome = nomeDaPessoa.getText().toString();
            String nick = nomeDeUsuario.getText().toString();

            Usuario usuario = new Usuario(nome, nick);

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(usuario);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Boolean isUsernameFree = Boolean.valueOf(reader.readLine());

            if (isUsernameFree) {
                informarConexao.setText("AEOH");
            } else {
                informarConexao.setText("Usuario ja utilizado, por favor, tente novamente");
            }

        } catch (IOException e) {
            informarConexao.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
            informarConexao.setText("Problema com o servidor, por favor, tente novamente.");
            e.printStackTrace();
        }
    }
}
