package com.example.socketsproject;

import android.app.Application;
import android.util.Log;

import com.example.socketsproject.utils.Constants;

import java.io.IOException;
import java.net.Socket;

public class SocketApplication extends Application {

    private static final String TAG = SocketApplication.class.getSimpleName();
    private static volatile Socket mSocket;

    public static Socket getSocket() {
        if(mSocket == null || mSocket.isClosed() || !mSocket.isConnected()) {
            try {
                mSocket = new Socket(Constants.IPV4, Constants.PORT_NUMBER);
                Log.d(TAG, "run: socket conectado: " + mSocket.toString());

            } catch (IOException e) {
                mSocket = null;
                e.printStackTrace();
            }
        }
        return mSocket;
    }
}
