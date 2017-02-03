package com.drecot.chopperattack;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;


public class Game extends Activity {

    MediaPlayer backgroundMusic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        backgroundMusic = MediaPlayer.create(Game.this, R.raw.game);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(10.0f,3.0f);

        setContentView(new GamePanel(this));

    }


    @Override
    public boolean onTouchEvent(MotionEvent bg) {
      //play music

        backgroundMusic.start();

        return super.onTouchEvent(bg);


    }
    public	boolean	onKeyDown(int	keyCode,	KeyEvent event)	{
        if	(keyCode	==	KeyEvent.KEYCODE_BACK)	{
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            finish();
            return	true;
        }
        return	false;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
}