package br.com.giroscopio.giroscopio;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;

public class ConnectThread extends Thread{

    private final OutputStream output;
    private final InputStream input;
    private byte[] comands;

    public ConnectThread(BluetoothSocket  socket){

        OutputStream outputTemp = null;
        InputStream inputTemp = null;
        comands = new byte[7];

        try{

            outputTemp = socket.getOutputStream();
            inputTemp = socket.getInputStream();

        }catch (Exception e){

        }

        output = outputTemp;
        input = inputTemp;

        Log.e(TAG, "Aviso: Mandando Test 3");
    }//cancel



    public void write(byte[] comands) {
        if (output != null) {
            try {
                Log.e(TAG, "Aviso: Mandando Test 4");
                //output.write(comands);
                output.write(comands);
                Log.e(TAG, "Aviso: Test enviado");
            } catch (IOException e) {
                Log.e(TAG, "Write exception", e);
                // Try to reconnect

            }
        } else {
            Log.e(TAG, "Output stream is null");

        }
    }


}//ConnectThread
