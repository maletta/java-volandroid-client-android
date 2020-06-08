package br.com.giroscopio.giroscopio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


public class DispositivosPareados extends AppCompatActivity {

    String mac;
    Bluetooth bt;
    ListView lista;
    ArrayAdapter<String> adapter;
    public static final String CHAVE = "Bluetooth";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos_pareados);

        lista = (ListView) findViewById(R.id.listDispositivos);
        bt = new Bluetooth();

        preencherLista();
        acaoLista();


    }//onCreate


    public void preencherLista(){

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        if (bt.getListDevices().size() > 0) {
            for (BluetoothDevice device : bt.getListDevices()) {
                adapter.add(device.getName() + "\n" + device.getAddress());
            }//for
        }//if

        lista.setAdapter(adapter);

    }//preencherLista

    //Acao ao selecionar um dispositivo da lista
    public void acaoLista(){

        //acao ao selecionar um item da lista
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {

                String temp = (String) adapter.getItem(position);

                mac = temp.substring(temp.indexOf("\n")+1, temp.length());

                startControle();

            }
        });//setOnItemClickListener

    }//acaoLista


    //Volta para a tela inicial
    public void voltar(View view){

        this.finish();

    }//voltar


    //inicia a tela que procura os novos dispositivos
    public  void procurarDispositivos(View view){

        Intent ProcurandoDispositivos = new Intent(this, ProcurandoDispositivos.class );
        startActivity(ProcurandoDispositivos);

    }//procurarDispositivos


    //abre a tela com o controle e conecta ao dispositivo
    public void startControle(){

        Intent controle = new Intent(this, ControleFixo.class);

        controle.putExtra(CHAVE, mac);

        startActivity(controle);

    }//listaJogos


}//classe
