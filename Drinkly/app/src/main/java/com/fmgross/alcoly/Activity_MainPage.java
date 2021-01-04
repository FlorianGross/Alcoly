package com.fmgross.alcoly;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmgross.alcoly.fragments.Fragment_Main;
import com.fmgross.alcoly.fragments.Fragment_Statistics;
import com.fmgross.alcoly.fragments.Fragment_Timeline;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_MainPage extends AppCompatActivity {

    ImageView timelineImage, scanImage, sessionImage;
    TextView timelineText, scanText, sessionText;
    ConstraintLayout timelineButton, scanButton, sessionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            showStartActivity();
        }

        timelineButton = findViewById(R.id.timelineButton);
        timelineImage = findViewById(R.id. timelineImage);
        timelineText = findViewById(R.id.timelineText);
        scanButton = findViewById(R.id.scanButton);
        scanImage = findViewById(R.id.scanImage);
        scanText = findViewById(R.id.scanText);
        sessionButton = findViewById(R.id.sessionButton);
        sessionImage = findViewById(R.id.sessionImage);
        sessionText = findViewById(R.id.sessionText);


        timelineButton.setOnClickListener(v -> {
            setAllfalse();
            timelineText.setVisibility(View.VISIBLE);
            timelineImage.setColorFilter(Color.argb(255,0,0,0));
            timelineButton.setBackgroundColor(Color.argb(255, 255,205,25));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new Fragment_Timeline()).commit();
        });
        scanButton.setOnClickListener(v -> {
            setAllfalse();
            scanText.setVisibility(View.VISIBLE);
            scanImage.setColorFilter(Color.argb(255,0,0,0));
            scanButton.setBackgroundColor(Color.argb(255, 255,205,25));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new Fragment_Main()).commit();
        });
        sessionButton.setOnClickListener(v -> {
            setAllfalse();
            sessionText.setVisibility(View.VISIBLE);
            sessionImage.setColorFilter(Color.argb(255,0,0,0));
            sessionButton .setBackgroundColor(Color.argb(255, 255,205,25));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new Fragment_Statistics()).commit();
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new Fragment_Main()).commit();
    }

    private void setAllfalse() {
        timelineText.setVisibility(View.GONE);
        sessionText.setVisibility(View.GONE);
        scanText.setVisibility(View.GONE);
        timelineButton.setBackgroundResource(R.color.MainBackground);
        sessionButton.setBackgroundResource(R.color.MainBackground);
        scanButton.setBackgroundResource(R.color.MainBackground);
        sessionImage.setColorFilter(Color.argb(255,255,255,255));
        scanImage.setColorFilter(Color.argb(255,255,255,255));
        timelineImage.setColorFilter(Color.argb(255,255,255,255));
    }


    /**
     * Starts the first Startup activity
     */
    private void showStartActivity() {
        Intent intent = new Intent(this, Activity_FirstStartUp.class);
        startActivity(intent);
    }
}