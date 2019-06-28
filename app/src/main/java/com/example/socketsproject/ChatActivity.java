package com.example.socketsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.socketsproject.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatActivity extends AppCompatActivity implements Runnable{

    private String mNomeUsuario;
    private static final String TAG = ChatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    public void testee(View view) {

        Thread thisThread = new Thread(this);
        if (!thisThread.isAlive()) {
            thisThread.start();
        }
    }

    @Override
    public void run() {
        try {
            Socket socket = SocketApplication.getSocket();
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

            Log.d(TAG, "run: Criando mensagem: ");

            JSONObject object = new JSONObject();

            object.put("type", "mensagem");
            object.put("mensagem", "HelloWorld!");

            printWriter.println(object.toString());

        } catch (Exception e) {
            Log.d(TAG, "run: EXCEPTION: " + e);
            e.printStackTrace();

        }
    }
}
