package com.drecot.chopperattack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import android.view.Window;
import android.view.WindowManager;


public class Game extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);




        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new GamePanel(this));




    }


    @Override
    public boolean onTouchEvent(MotionEvent bg) {

        return super.onTouchEvent(bg);


    }
    @Override
    public void onPause() {
        super.onPause();



    }
    @Override
    public void onResume() {
        super.onResume();


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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_game, menu);
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