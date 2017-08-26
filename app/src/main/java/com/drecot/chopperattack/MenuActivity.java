package com.drecot.chopperattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;



import com.google.firebase.messaging.FirebaseMessaging;

import com.drecot.chopperattack.app.Config;
import com.drecot.chopperattack.util.NotificationUtils;

public class MenuActivity extends Activity {
    private static final String TAG = MenuActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;
    MediaPlayer menu;
    int volume2;
    private int sound1 = 1;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menu = MediaPlayer.create(this, R.raw.menu);

        menu.setLooping(true);
        menu.setVolume(1.0f, 1.0f);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_menu);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        txtRegId = (TextView) findViewById(R.id.txt_reg_id);
        txtMessage = (TextView) findViewById(R.id.txt_push_message);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();






        //volume
        SharedPreferences pref = getSharedPreferences("higher", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        volume2=pref.getInt("vloume2", 1);
        if(volume2==0)
        {
            sound1=0;

        }if (sound1 ==1){
            menu.start();
        }

    }
    @Override
    public void onStart(){
        super.onStart();

    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


            public void play(View v)
            {
                if (sound1==1){
                    menu.stop();
                    menu.release();
                    menu=null;
                }

            Toast.makeText(this, "Game Opened.", Toast.LENGTH_SHORT).show();


                Intent i=new Intent(this,Game.class);
                startActivity(i);
            }

            public void highscore(View v)
            {

                Intent i=new Intent(this,ScoresActivity.class);
                startActivity(i);

            }

            public void setting(View v)
            {

                Intent i=new Intent(this,SettingsActivity.class);
                startActivity(i);
            }

            public void help(View v)
            {

                Intent i=new Intent(this,HelpActivity.class);
                startActivity(i);
            }

            public void exit(View v)
            {

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.quit)
                        .setMessage(R.string.really_quit)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                menu.stop();
                                menu.release();
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);

                                //Stop the activity

                            }

                        })
                        .setNegativeButton(R.string.no, null)
                        .show();

            }

    public	boolean	onKeyDown(int	keyCode,	KeyEvent event)	{
        if	(keyCode	==	KeyEvent.KEYCODE_BACK)	{

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.quit)
                    .setMessage(R.string.really_quit)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            menu.stop();
                            menu.release();

                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);


                            //Stop the activity


                        }

                    })
                    .setNegativeButton(R.string.no, null)
                    .show();

            return	true;
        }
        return	false;
    }

}
