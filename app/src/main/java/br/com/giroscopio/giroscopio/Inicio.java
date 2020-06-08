package br.com.giroscopio.giroscopio;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;



public class Inicio extends AppCompatActivity {

    private SensorManager sensorM;
    private Sensor gyro;
    private SensorEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        PhoneGyroscope pg = new PhoneGyroscope(this);

        sensorM = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyro = sensorM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (gyro == null) {
            Toast.makeText(this, "Sem SENSOR", Toast.LENGTH_SHORT).show();
            //finish();
        }else{
            Toast.makeText(this, "Com SENOR", Toast.LENGTH_SHORT).show();
        }


        listener =  new SensorEventListener(){
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {


//                if(sensorEvent.values[2] >0.5f){
//                        getWindow().getDecorView().setBackgroundColor(Color.BLUE);
//                }else if (sensorEvent.values[2] < -0.5f)
//                {
//                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
//                }
                TextView id0 = (TextView)findViewById(R.id.id0);
                TextView id1 = (TextView)findViewById(R.id.id1);
                TextView id2 = (TextView)findViewById(R.id.id2);

                id0.setText(String.valueOf(sensorEvent.values[0]));
                id1.setText(String.valueOf(sensorEvent.values[1]));
                id2.setText(String.valueOf(sensorEvent.values[2]));


            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }
    @Override
    public void onResume() {
        super.onResume();
        sensorM.registerListener(listener,gyro,SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause()
    {
        super.onPause();
            sensorM.unregisterListener(listener);
    }



}
