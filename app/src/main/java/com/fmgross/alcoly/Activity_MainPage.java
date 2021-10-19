package com.fmgross.alcoly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.fmgross.alcoly.fragments.Fragment_Main;
import com.fmgross.alcoly.fragments.Fragment_Statistics;
import com.fmgross.alcoly.fragments.Fragment_Timeline;
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
            timelineImage.setColorFilter(getResources().getColor(R.color.reverseText, null));
            timelineButton.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new Fragment_Timeline()).commit();
        });
        scanButton.setOnClickListener(v -> {
            setAllfalse();
            scanText.setVisibility(View.VISIBLE);
            scanImage.setColorFilter(getResources().getColor(R.color.reverseText, null));
            scanButton.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new Fragment_Main()).commit();
        });
        sessionButton.setOnClickListener(v -> {
            setAllfalse();
            sessionText.setVisibility(View.VISIBLE);
            sessionImage.setColorFilter(getResources().getColor(R.color.reverseText, null));
            sessionButton.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new Fragment_Statistics()).commit();
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new Fragment_Main()).commit();
    }

    private void setAllfalse() {
        timelineText.setVisibility(View.GONE);
        sessionText.setVisibility(View.GONE);
        scanText.setVisibility(View.GONE);
        timelineButton.setBackgroundColor(getResources().getColor(R.color.MainBackground, null));
        sessionButton.setBackgroundColor(getResources().getColor(R.color.MainBackground, null));
        scanButton.setBackgroundColor(getResources().getColor(R.color.MainBackground, null));
        sessionImage.setColorFilter(getResources().getColor(R.color.text, null));
        scanImage.setColorFilter(getResources().getColor(R.color.text, null));
        timelineImage.setColorFilter(getResources().getColor(R.color.text, null));
    }


    /**
     * Starts the first Startup activity
     */
    private void showStartActivity() {
        Intent intent = new Intent(this, Activity_FirstStartUp.class);
        startActivity(intent);
    }
}