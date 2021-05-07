package com.fmgross.alcoly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.fmgross.alcoly.backend.Backend_ProgressBar;

public class Activity_Main extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setScaleY(3f);
        SharedPreferences defaultSettings = PreferenceManager.getDefaultSharedPreferences(this);
        if (defaultSettings.getBoolean("Night", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        progressAnimation(2000);

    }

    /**
     * Creates and starts the progress Animation with the time of
     *
     * @param time the time, the progressBar needs to finish
     */
    public void progressAnimation(int time) {
        Backend_ProgressBar anim = new Backend_ProgressBar(this, progressBar, 0f, 100f);
        anim.setDuration(time);
        progressBar.setAnimation(anim);
    }
}