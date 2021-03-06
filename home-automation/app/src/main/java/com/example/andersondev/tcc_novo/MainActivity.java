package com.example.andersondev.tcc_novo;

import android.animation.Animator;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout layoutCoord;
    public static final int MESSAGE_READ = 3;

    /* TTS */

    Speech speech;
    private LottieAnimationView animationView;
    BancoDados db;
    Switch outMode;
    Cursor cursor;
    /* Bluetooth */

    Bluetooth bluetooth;
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private boolean hasRow = false;

    /* Controle */
    ImageButton btnControl;

    /* Temperature */

    ImageButton btnTemperature;

    /* WaterBox */

    ImageButton btnWaterBox;

    /* SHOWER */
    ImageButton btnShower;
    /**
     * When it's being created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /* BLUETOOTH */

        bluetooth = new Bluetooth(getApplicationContext(),uuid,this,0);
        bluetooth.create();

        speech =  new Speech(this, getApplicationContext(),bluetooth);

        setContentView(R.layout.controle);

        btnControl  = (ImageButton) findViewById(R.id.btnControl);
        btnTemperature = (ImageButton) findViewById(R.id.btnTemperature);
        btnWaterBox = (ImageButton) findViewById(R.id.btnWaterBox);
        btnShower = (ImageButton)findViewById(R.id.btnChart);
        outMode = (Switch)findViewById(R.id.outMode);
        db = new BancoDados(getApplicationContext());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        animationView = findViewById(R.id.mic);
        if(db.hasRowSetting()) {
            cursor = db.getSetting();
            outMode.setChecked(Boolean.parseBoolean(cursor.getString(3)));
        }

        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animationView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        btnShower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.closeConn();
                Intent it = new Intent(MainActivity.this, Shower.class);
                startActivity(it);
            }
        });

        /* Mic button Event */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                animationView.playAnimation();
                if(speech.isAvailableRecognizer()) {

                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, new Locale("pt", "BR"));

                    speech.mySR.startListening(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Desculpe, mas seu dispositivo não suporta reconhecimento de voz", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* TTS FUNCTIONS*/
        speech.initializeTextToSpeech();
        speech.initializeSpeechRecognizer();

        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.closeConn();
                Intent it = new Intent(MainActivity.this, Control.class);
                startActivity(it);
            }
        });

        btnTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.closeConn();
                Intent it = new Intent(MainActivity.this, Temperature.class);
                startActivity(it);
            }
        });

        btnWaterBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.closeConn();
                Intent it = new Intent(MainActivity.this, WaterBox.class);
                startActivity(it);
            }
        });

        outMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cursor = db.getSetting();
                    Settings set = new Settings(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Double.parseDouble(cursor.getString(2)), true);
                    db.updateSetting(set);
                }
                else{
                    cursor = db.getSetting();
                    Settings set = new Settings(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Double.parseDouble(cursor.getString(2)), false);
                    db.updateSetting(set);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bluetooth.result(requestCode,resultCode,data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onPause(){
        super.onPause();
        //speech.myTTS.shutdown();
    }
}
