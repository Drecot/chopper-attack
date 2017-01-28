package com.drecot.chopperattack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.io.InputStream;
import android.widget.TextView;



public class HelpActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        InputStream iFile = getResources().openRawResource(R.raw.help);
        TextView helpText = (TextView) findViewById(R.id.helpscreen);
        String strFile = inputStreamToString(iFile);
        helpText.setText(strFile);
    }

    private String inputStreamToString(InputStream b) {
        return null;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();

    }


}
