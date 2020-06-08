package br.com.giroscopio.giroscopio;


import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class Config extends AppCompatActivity {

    SeekBar barraSensibilidade;
    TextView valorSensibilidade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        barraSensibilidade = (SeekBar) findViewById(R.id.barraSensibilidade);
        valorSensibilidade = (TextView) findViewById(R.id.sensibilidade);

        barraSensibilidade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                valorSensibilidade.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }//onCreate

    public void voltar(View view){

        this.finish();

    }//voltar

}//Config
