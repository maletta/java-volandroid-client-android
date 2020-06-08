package br.com.giroscopio.giroscopio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ProcurandoDispositivos extends AppCompatActivity {

    ArrayAdapter<String> mArrayAdapter;
    ListView list;
    Bluetooth bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurando_dispositivos);

        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        bt = new Bluetooth();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }

        bt.procuraDispositivos();

        list = (ListView) findViewById(R.id.listDispositivos);
        list.setAdapter(mArrayAdapter);

        //Registra BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

    }//onCreate


    public void acaoLista(){

        //acao ao selecionar um item da lista
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {

                String temp = (String) mArrayAdapter.getItem(position);
                String mac = temp.substring(temp.indexOf("\n")+1, temp.length());

                Toast.makeText(getApplicationContext(), "Info:" + position + "\n" + mac, Toast.LENGTH_SHORT).show();

                bt.setMacDevice(mac);
                bt.startConnection();

                Toast.makeText(getApplicationContext(), "Coneção com o :" + mac + "efetuada com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }//acaoLista

    @Override
    protected void onDestroy() {

        super.onDestroy();
        //Remove o filtro de descoberta de dispositivos do registro
        unregisterReceiver(mReceiver);

    }//onDestroy


    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // Quando encontra um dispositivo bt
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                //Pega o dispositivo bt da intent e Adiciona o nome e endereco do dispositovo encontrado no array
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());

            }//if

        }//onReceive

    };//BroadcastReceiver


    public void voltar(View view){

        this.finish();

    }//voltar

}//ProcurandoDispositivos



