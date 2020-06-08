package br.com.giroscopio.giroscopio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class Controle extends AppCompatActivity implements View.OnTouchListener , SensorEventListener {


    public static final String CHAVE = "Bluetooth";
    private String mac;
    private Bluetooth bluetooth;
    private int comandos[];
    private Thread update;
    private Sensor accelerometer;
    private SensorManager sensorManager;
    private Intent telaPause;



    TextView text_eixo_z, text_eixo_x, text_eixo_y;

    private int eixo_x, eixo_y, eixo_z, status;

    private int sensibilidade = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle);

        starConnection();


        comandos = new int[9];
        comandos[0] = 1;

        for(int i=1; i<comandos.length ;i++){
            comandos[i] = 0;
        }

        addButtonsListener();

        iniciarSensores();

        telaPause = new Intent(this, Pause.class);
    }//onCreate


    public void getDados(){

        String a = "";

        for(int i=0; i<comandos.length ;i++){
            if(comandos[i] == 0){
                a = a + "0";
            }else{
                a = a + "1";
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
        comandos[0] = 1;
        String mensagem = "";
        for(int j = 0; j<comandos.length-1; j++){
            mensagem += String.valueOf(comandos[j]);
        }
        bluetooth.write(mensagem.getBytes());
        //comandos[1] = 1;
        //bluetooth.write(comandos); envio original - maletta comentou
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

        int p = 0;

        switch(id){

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

    //Metodos que utilizam os sensores de covimento -----------------------------------------------------------------------------------
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

        eixo_y = Math.round(event.values[1]);

        text_eixo_z.setText(String.valueOf(Math.round( event.values[2])));
        text_eixo_y.setText(String.valueOf(eixo_y));
        text_eixo_x.setText(String.valueOf(Math.round(event.values[0])));

        //4=direita
        //3=esquerda
        if(eixo_y>sensibilidade || eixo_y <-sensibilidade){
            if(eixo_y>0) {
                comandos[4] = 1;
                comandos[3] = 0;

                if (status != 1){
                    uptadeData();
                }
                status = 1;

            }else{
                comandos[4] = 0;
                comandos[3] = 1;

                if (status != 2){
                    uptadeData();
                }

                status = 2;
            }

        }else{
            comandos[4] = 0;
            comandos[3] = 0;

            if (status != 3){
                uptadeData();
            }

            status = 3;
        }
    }//onSensorChanged

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }//onAccuracyChanged

}//ControleFixo

