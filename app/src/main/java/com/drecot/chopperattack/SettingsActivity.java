package com.drecot.chopperattack;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsActivity extends Activity {
    int volume;
    int volume2;
    ImageButton btnSwitch;
    ImageButton btnSwitch1;
    private boolean isFlashOn;
    private boolean isSoundOn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_settings);

        TextView tv = (TextView) findViewById(R.id.textView1);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/GoodDog.otf");
        tv.setTypeface(tf);



        btnSwitch = (ImageButton) findViewById(R.id.sound);
        btnSwitch1 = (ImageButton) findViewById(R.id.music);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("higher", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        volume=pref.getInt("vloume", 1);
        volume2=pref.getInt("vloume2", 1);


        if(volume==1)
        {
            isFlashOn = true;
            turnOnVolume();

        }
        else {
            isFlashOn=false;
            turnOffVolume();

        }

        if(volume2==1)
        {
            isSoundOn = true;
            turnOnSound();

        }
        else {
            isSoundOn=false;
            turnOffSound();

        }

        btnSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("higher", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                if (volume == 1) {
                    // turn off flash
                    turnOffVolume();
                    isFlashOn=false;
                    volume=0;
                    editor.putInt("vloume", 0);
                    editor.commit();
                } else {
                    // turn on flash
                    turnOnVolume();
                    isFlashOn=true;
                    volume=1;
                    editor.putInt("vloume", 1);
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"sound effects on", Toast.LENGTH_LONG).show();

                }
            }
        });

        btnSwitch1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("higher", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                if (volume2 == 1) {
                    // turn off flash
                    turnOffSound();
                    isSoundOn=false;
                    volume2=0;
                    editor.putInt("vloume2", 0);
                    editor.commit();
                } else {
                    // turn on flash
                    turnOnSound();
                    isSoundOn=true;
                    volume2=1;
                    editor.putInt("vloume2", 1);
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"Menu music on", Toast.LENGTH_LONG).show();

                }
            }
        });
    }



    private void turnOnVolume() {
        volume = 1;
        btnSwitch.setImageResource(R.drawable.soundon);
        isFlashOn= true;
    }



    private void turnOffVolume() {
        btnSwitch.setImageResource(R.drawable.soundoff);
        volume = 0;
        isFlashOn = false;
    }

    private void turnOnSound() {
        volume2 = 1;
        btnSwitch1.setImageResource(R.drawable.musicon);
        isSoundOn= true;
    }



    private void turnOffSound() {
        btnSwitch1.setImageResource(R.drawable.musicoff);
        volume2 = 0;
        isSoundOn = false;
    }


    public void back(View v)
    {
        finish();
    }

}
