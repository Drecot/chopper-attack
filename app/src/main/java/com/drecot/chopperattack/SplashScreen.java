package com.drecot.chopperattack;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreen extends Activity{

    //timer//
    private static int SPLASH_TIME_OUT = 2000;

    //create first screen showed when app is launched//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            //showing splashscreen with a timer //

            @Override
            public void run() {
                //this is executed once the timer is over//

                Intent i = new Intent(SplashScreen.this, MenuActivity.class);
                startActivity(i);
                finish();

            }
        },SPLASH_TIME_OUT);

    }
}