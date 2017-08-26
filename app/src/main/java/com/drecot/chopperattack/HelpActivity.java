package com.drecot.chopperattack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import java.io.InputStream;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


public class HelpActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_help);

        TextView tv = (TextView) findViewById(R.id.helpscreen);
        TextView ta = (TextView) findViewById(R.id.textView);
        TextView th = (TextView) findViewById(R.id.textView1);
        TextView tb = (TextView) findViewById(R.id.textView2);
        TextView tc = (TextView) findViewById(R.id.textView3);
        TextView td = (TextView) findViewById(R.id.textView23);
        TextView te = (TextView) findViewById(R.id.textView4);
        TextView tg = (TextView) findViewById(R.id.textView5);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/GoodDog.otf");
        tv.setTypeface(tf);
        ta.setTypeface(tf);
        tb.setTypeface(tf);
        tc.setTypeface(tf);
        td.setTypeface(tf);
        te.setTypeface(tf);
        tg.setTypeface(tf);
        th.setTypeface(tf);


    }

    public void back(View v)
    {
       finish();
    }




}
