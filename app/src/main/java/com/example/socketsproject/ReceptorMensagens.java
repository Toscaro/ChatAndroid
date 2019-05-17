package com.example.socketsproject;

import android.util.Log;

import java.io.InputStream;
import java.util.Scanner;

public class ReceptorMensagens implements Runnable {

    private final String TAG = getClass().getSimpleName();

    private InputStream mInputStream;

    public ReceptorMensagens(InputStream inputStream) {
        this.mInputStream = inputStream;
    }

    @Override
    public void run() {

        Log.d(TAG, "run: awaiting for messages");
        Scanner scanner = new Scanner(mInputStream);
        while (scanner.hasNextLine()) {
            Log.d(TAG, "run: mensagem: " + scanner.nextLine());
        }
    }
}
