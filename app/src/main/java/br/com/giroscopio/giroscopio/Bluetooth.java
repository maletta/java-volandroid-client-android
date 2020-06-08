package br.com.giroscopio.giroscopio;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.app.ListActivity;

import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class Bluetooth extends ListActivity {


    BluetoothAdapter mBluetoothAdapter;

    //Connection Atributes
    ConnectThread connectThread;
    String macDevice;
    BluetoothSocket btSocket;
    BluetoothDevice device;
    Boolean running =  false;
    String myUUID = "00001101-0000-1000-8000-00805F9B34FB";


    String status = "";

    public Bluetooth(){

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }//end build

    public BluetoothAdapter getBluetoothAdapter(){
        return mBluetoothAdapter;
    }//end getBluetoothAdapter

    //Verifica se o bt esta ativo
    public boolean checkBluetoothEnable(){

        if (!mBluetoothAdapter.isEnabled()) {
            return true;
        }//end if

        return false;

    }//end cleckBluetoothEnable


    public void setMacDevice(String mac){
        macDevice = mac;
    }//setMacDevice


    public void checkBluetoothDevice(){

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            System.exit(0);
        }//end if

    }//end checkBluetoothDevice


    public Set<BluetoothDevice> getListDevices(){

        Set<BluetoothDevice> pairedDevices = getBluetoothAdapter().getBondedDevices();

        return pairedDevices;
    }//end getListDevices


    //Pede permissao para procurar novos dispositivos
    public void permissaoProcurar(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }

    }//permissaoProcurar


    //inicia a descoberta de novos dispositivos
    public void procuraDispositivos(){

        mBluetoothAdapter.startDiscovery();

    }//procuraDispositivos


    //Inicia a procura de novos dispositivos
    public void  cancelarProcura(){

        mBluetoothAdapter.cancelDiscovery();

    }//cancelarProcura



    public BluetoothDevice getBtDevice( ){
        return mBluetoothAdapter.getRemoteDevice(macDevice);
    }//getBtDevice


    public void startConnection(){
        if(!running) {

            cancelarProcura();

            try{
                btSocket = getBtDevice().createRfcommSocketToServiceRecord(UUID.fromString(myUUID));
                connectThread = new ConnectThread(btSocket);
            }catch (Exception e){

            }

            try{
                btSocket.connect();
            }catch (Exception e) {

                try {

                    btSocket = (BluetoothSocket) getBtDevice().getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(getBtDevice(), 1);
                    btSocket.connect();
                } catch (Exception d) {

                }
            }


            running = true;
            //connectThread.start();
        }//if
    }//startConnection

    public void write(byte[] comandos) {
        Log.e(TAG, "Aviso: Mandando Test 2");
        connectThread.write(comandos);
    }//enviar

    public void disconnect(){
        try {
            btSocket.close();
        }catch (Exception e){}
    }

    public void reconnect() {

        disconnect();
        startConnection();

    }//reconect


}//Bluetooth
