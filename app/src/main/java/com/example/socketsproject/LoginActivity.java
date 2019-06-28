package com.example.socketsproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

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

        mContext = this;

        nomeDaPessoa = findViewById(R.id.nome_da_pessoa_edt);
        informarConexao = findViewById(R.id.informar_conexao_txt);
        conectarButton = findViewById(R.id.conectar_btn);

        conectarButton.setOnClickListener(buttonClickListener);

    }

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Thread iniciarConexao = new Thread(LoginActivity.this);

            if (!iniciarConexao.isAlive()) {
                iniciarConexao.start();
            }
        }
    };

    @Override
    public void run() {
        informarConexao.setTextColor(mContext.getColor(R.color.colorBlack));
        informarConexao.setText("Conectado com o servidor, aguardando...");

        try {
            Socket socket = SocketApplication.getSocket();
            if (socket != null) {

                ReceptorMensagens receptorMensagens = ReceptorMensagens.getInstance();
                receptorMensagens.setJsonListener(onLoginReceivedListener);

                Thread receptorDeMensagensThread = new Thread(receptorMensagens);

                if (!receptorDeMensagensThread.isAlive()) {
                    receptorDeMensagensThread.start();
                }

                Log.d(TAG, "run: printStream");
                PrintStream printStream = new PrintStream(socket.getOutputStream());
                printStream.println(gerarJsonDeLogin());
            }
        } catch (Exception e) {

            e.printStackTrace();
            informarConexao.setTextColor(mContext.getColor(R.color.colorRed));
            informarConexao.setText("Problema com o servidor, por favor, tente novamente..");
        }
    }

    private String gerarJsonDeLogin() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","login");
        jsonObject.put("nome", nomeDaPessoa.getText());

        return jsonObject.toString();
    }

    OnLoginReceived onLoginReceivedListener = new OnLoginReceived() {
        @Override
        public void onLoginReceived(boolean deveConectar) {
            Log.d(TAG, "onLoginReceived: deveConectar: " + deveConectar);

            if (deveConectar) {
                startActivity(
                        new Intent(LoginActivity.this, ChatActivity.class)
                                .putExtra("nomeUsuario", nomeDaPessoa.toString()));



//                LoginActivity.this.finish();

            } else {
                informarConexao.setTextColor(mContext.getColor(R.color.colorRed));
                informarConexao.setText("Outro usuario conectado com o mesmo nome, favor, utilizar outro.");
            }

        }
    };

}
