package com.drecot.chopperattack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import java.io.InputStream;

import android.view.KeyEvent;
import android.widget.TextView;



public class HelpActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
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


}
