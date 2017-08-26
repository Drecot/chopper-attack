package com.drecot.chopperattack;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;



//Our class extending fragment
public class Tab1 extends Fragment {


    TextView textView,textView2,textView3,textView4, textView5;

    SharedPreferences sharedPreferences;
    //Override method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View myInflatedView = inflater.inflate(R.layout.tab1, container,false);


        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GoodDog.otf");



        textView = (TextView) myInflatedView.findViewById(R.id.textView4);
        textView2 = (TextView) myInflatedView.findViewById(R.id.textView6);
        textView3 = (TextView) myInflatedView.findViewById(R.id.textView8);
        textView4 = (TextView) myInflatedView.findViewById(R.id.textView10);
        textView5 = (TextView) myInflatedView.findViewById(R.id.textView12);

        sharedPreferences  = getContext().getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        textView.setText("1.  "+sharedPreferences.getInt("score1",0));
        textView2.setText("2.  "+sharedPreferences.getInt("score2",0));
        textView3.setText("3.  "+sharedPreferences.getInt("score3",0));
        textView4.setText("4.  "+sharedPreferences.getInt("score4",0));
        textView5.setText("5.  "+sharedPreferences.getInt("score5",0));

        textView.setTypeface(tf);
        textView2.setTypeface(tf);
        textView3.setTypeface(tf);
        textView4.setTypeface(tf);
        textView5.setTypeface(tf);

        return myInflatedView;

    }



}