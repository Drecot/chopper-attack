package com.drecot.chopperattack;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;


public class MenuActivity extends Activity {
    Button button;
    Button button1;
    Button button2;
    MediaPlayer backgroundMusic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backgroundMusic = MediaPlayer.create(this, R.raw.menu);

        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(10.0f,3.0f);
        backgroundMusic.start();

        setContentView(R.layout.activity_menu);
        addListenerOnButton();

    }




    public void addListenerOnButton() {

        final Context context = this;

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                backgroundMusic.release();
                backgroundMusic=null;
                Intent intent = new Intent(context, Game.class);
                startActivity(intent);
                finish();
                Toast.makeText(context, "Game Opened.", Toast.LENGTH_SHORT).show();

            }

        });
        button1 = (Button) findViewById(R.id.button4);

        button1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, HelpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        button2 = (Button) findViewById(R.id.button3);

        button2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, ScoresActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
