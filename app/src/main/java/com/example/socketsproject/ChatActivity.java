package com.example.socketsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.socketsproject.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatActivity extends AppCompatActivity implements Runnable{

    private Socket mSocket;
    private String mNomeUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    public void teste(View view) {
        new Thread(ChatActivity.this).start();

    }

    public void testee(View view) {

        try {
                new PrintWriter(mSocket.getOutputStream()).println(new JSONObject()
                                .put("type", "mensagem")
                                .put("mensagem", "Ola mundo!!!"));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void run() {
        try {
            mSocket = new Socket(Constants.IPV4, Constants.PORT_NUMBER);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
