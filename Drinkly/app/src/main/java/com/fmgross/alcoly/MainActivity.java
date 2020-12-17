package com.fmgross.alcoly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.fmgross.alcoly.backend.ProgressBarAnimation;

public class MainActivity extends AppCompatActivity {

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
        SharedPreferences.Editor editor = defaultSettings.edit();

        progressAnimation(2000);

    }

    /**
     * Creates and starts the progress Animation with the time of
     *
     * @param time the time, the progressBar needs to finish
     */
    public void progressAnimation(int time) {
        ProgressBarAnimation anim = new ProgressBarAnimation(this, progressBar, 0f, 100f);
        anim.setDuration(time);
        progressBar.setAnimation(anim);
    }
}