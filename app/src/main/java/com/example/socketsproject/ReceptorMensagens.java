package com.example.socketsproject;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReceptorMensagens implements Runnable {

    private final String TAG = getClass().getSimpleName();

    private final InputStream mInputStream;
    private OnLoginReceived mTest;

    private static volatile ReceptorMensagens mReceptorMensagensInstance;

    public static ReceptorMensagens getInstance() {

        if (mReceptorMensagensInstance == null) {
            try {
                mReceptorMensagensInstance = new ReceptorMensagens(SocketApplication.getSocket().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mReceptorMensagensInstance;
    }
    ReceptorMensagens(InputStream inputStream) {
        this.mInputStream = inputStream;

    }

    void setJsonListener(OnLoginReceived test) {
        this.mTest = test;
    }

    @Override
    public void run() {
        try {

            Log.d(TAG, "run: awaiting for messages");
            BufferedReader reader = new BufferedReader(new InputStreamReader(mInputStream));

            //noinspection InfiniteLoopStatement
            while (true) {
                String novaMensagem = reader.readLine();


                if (novaMensagem != null && !novaMensagem.isEmpty()) {

                    Log.d(TAG, "run: mensagem: " + novaMensagem);
                    receptorDeJson(novaMensagem);
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receptorDeJson(String json) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            Log.e(TAG, "receptorDeJson: Erro ao criar json recebido");
            e.printStackTrace();
            return;
        }

        String type = jsonObject.optString("type");

        if (type.equals("login")) {
            boolean deveConectar = jsonObject.optBoolean("login");
            mTest.onLoginReceived(deveConectar);

        } else if (type.equals("mensagem")){
            Log.d(TAG, "receptorDeJson: NOVA MENSAGEM!!");
        }

    }
}
