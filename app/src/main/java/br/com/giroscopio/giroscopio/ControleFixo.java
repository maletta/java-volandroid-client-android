package br.com.giroscopio.giroscopio;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class ControleFixo extends AppCompatActivity implements View.OnTouchListener, SensorEventListener {

    /*Detalhe do array
      0.cima
      1.baixo
      2.esquerda
      3.direita
      4.frear
      5.pause
      */

    public static final String CHAVE = "Bluetooth";
    private String mac;
    private Bluetooth bluetooth;
    private int comandos[];
    private Thread update;
    private Sensor accelerometer;
    private SensorManager sensorManager;
    private int pontoMorto =2;

    TextView text_eixo_z, text_eixo_x, text_eixo_y;
    private int x,y,z,y_anterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle_fixo);

        CheckBox giroCheck = findViewById(R.id.btnGiroscopio);
        giroCheck.setChecked(false);//desativar giroscópio ao iniciar o app
        y_anterior = 0;

        starConnection();


        comandos = new int[9];

        for(int i=0; i<comandos.length ;i++){
            comandos[i] = 0;
        }

        addButtonsListener();
        iniciarSensores();
        //updateThread();

    }//onCreate

    public void getDados(){

        String a = "";

        for(int i=0; i<comandos.length ;i++){
            if(comandos[i] == 0){
                a = a + "0";
            }else{
                a = a + String.valueOf(comandos[i]);
            }
        }

        TextView text = (TextView) findViewById(R.id.comandos);
        text.setText(a);

    }


    public void updateThread(){

        update = new Thread(){
            public void run(){
                while (true ){
                    uptadeData();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        update.start();

    }


    public void addButtonsListener(){

        findViewById(R.id.direita).setOnTouchListener(this);
        findViewById(R.id.esquerda).setOnTouchListener(this);
        findViewById(R.id.acelerar).setOnTouchListener(this);
        findViewById(R.id.frear).setOnTouchListener(this);
        findViewById(R.id.pause).setOnTouchListener(this);

    }//addButtonsListener

    public void uptadeData(){

        String mensagem = "";
        for(int j = 0; j<comandos.length-1; j++){
            mensagem += String.valueOf(comandos[j]);
        }
        bluetooth.write(mensagem.getBytes());
        //bluetooth.write(comandos);envio original - maletta comentou

    }

    private void starConnection(){
        mac = getIntent().getStringExtra(DispositivosPareados.CHAVE);
        bluetooth = new Bluetooth();
        bluetooth.setMacDevice(mac);
        bluetooth.startConnection();
        Toast.makeText(getApplicationContext(), "Conectado no dispositivo: "+ mac, Toast.LENGTH_SHORT).show();
    }

    public void send_test(View view){
        //comandos[0] = 1;
        String mensagem = "";
        for(int j = 0; j<comandos.length-1; j++){
            mensagem += String.valueOf(comandos[j]);
        }
        bluetooth.write(mensagem.getBytes());
        //comandos[1] = 1;
        //bluetooth.write(comandos); envio original - maletta comentou
    }

    public void ativarDesativarGiroscopio(View view)
    {
        if(((CheckBox) view).isChecked()){
            comandos[0] = 1;
        }else{
            comandos[0] = 0;
            comandos[3] = 0;
            comandos[4] = 0;
        }
        getDados();
        uptadeData();
    }

    @Override
    public final boolean onTouch(View view, MotionEvent event) {
        String id = view.getResources().getResourceEntryName(view.getId());

        if (event.getAction() == MotionEvent.ACTION_DOWN ) {
            comandos[posicao(id)] = 1;
            uptadeData();
            getDados();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            comandos[posicao(id)] = 0;
            uptadeData();
            getDados();
        }

        return true;

    }//onTouch

    private int posicao(String id){

        int p = 1;

        switch(id){
            case "direita":
                p =  4;
                break;
            case "esquerda":
                p =  3;
                break;
            case "acelerar":
                p =  1;
                break;
            case "frear":
                p =  2;
                break;
            case "pause":
                p =  6;
                break;
        }
        return  p;
    }//posicao

    public void reconectBt(View view){
        bluetooth.reconnect();
    }//reconect

    public void iniciarSensores(){

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        text_eixo_z = (TextView) findViewById(R.id.eixoz);
        text_eixo_x = (TextView) findViewById(R.id.eixox);
        text_eixo_y = (TextView) findViewById(R.id.eixoy);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        x = Math.round( event.values[0]);
        y = Math.round( event.values[1]);
        z = Math.round( event.values[2]);
        text_eixo_x.setText(String.valueOf(x));
        text_eixo_y.setText(String.valueOf(y));
        text_eixo_z.setText(String.valueOf(z));

        if(comandos[0] == 1) { //se giroscopio estiver ativado
            if (y_anterior != y) { //se o eixo y arrendondado foi alterado
                //ponto morto
                if(y<-pontoMorto || y>pontoMorto) {//ponto morto, para de apertar os botoes

                    if (y < 0 && y <y_anterior) { //ser y é negativo então vai pra esquerda, se y positivo então vai pra direita
                        if (y < -9) {//garante que só terá 1 dígito [máximo = 9], evita bug no array de comandos
                            comandos[3] = 9;
                        } else {
                            comandos[3] = Math.abs(y); //Maath.abs pega o valor absoluto, ou seja, elimina o sinal, evita bug no array de comandos
                        }
                        comandos[4] = 0;
                    } else {
                        if(y > 0 && y>y_anterior) {
                            if (y > 9) {//garante que só terá 1 dígito [máximo = 9], evita bug no array de comandos
                                comandos[4] = 9;
                            } else {
                                comandos[4] = Math.abs(y);//Maath.abs pega o valor absoluto, ou seja, elimina o sinal, evita bug no array de comandos
                            }
                            comandos[3] = 0;
                        }else{
                            comandos[3] = 0;
                            comandos[4] = 0;
                        }
                    }

                    y_anterior = y;
                }else{
                    comandos[3] = 0;
                    comandos[4] = 0;


                }

                getDados();
                uptadeData();


            }


        }////se giroscopio estiver ativado

    }//onSensorChanged

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }//onAccuracyChanged

}//ControleFixo
