package com.fmgross.drinkly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.fmgross.drinkly.backend.ProgressBarAnimation;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setScaleY(3f);
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