package br.com.giroscopio.giroscopio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TelaInicial2 extends AppCompatActivity {

    public static int ENABLE_BLUETOOTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        check();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial2);






    }//onCreate






    public void check(){

        Bluetooth bt = new Bluetooth();

        //Verifica se o dispositivo possue bt, caso nao a aplicação sera fechada
        bt.checkBluetoothDevice();

        //Verifica se o bt esta ativo, caso nao solicita que ative
        if(bt.checkBluetoothEnable()){
            Intent enableBtIntent = new Intent(bt.getBluetoothAdapter().ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH);
        }

    }//end checkBt


    public void gameList(View view){
        //Intent gameList = new Intent(this, ListaJogos.class);
        Intent controle = new Intent(this, Controle.class);
        startActivity(controle);
    }//listaJogos

    public void config(View view){
        Intent config = new Intent(this, Config.class);
        startActivity(config);
    }//config


    public void searchPairedDevices(View view) {

        Intent searchPairedDevicesIntent = new Intent(this, DispositivosPareados.class);
        startActivity(searchPairedDevicesIntent);

    }//fim searchPairedDevices


}//TelaInicial2
