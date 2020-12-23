package com.fmgross.alcoly.old;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmgross.alcoly.Activity_Camera;
import com.fmgross.alcoly.Activity_FirstStartUp;
import com.fmgross.alcoly.R;
import com.fmgross.alcoly.backend.Backend_Calculation;

import java.text.DecimalFormat;


public class Activity_MainScreen extends AppCompatActivity {
    private TextView textView;
    private final boolean running = true;
    private ImageView mainButton, leftB, centerB, rightB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        mainButton = findViewById(R.id.mainAction);
        leftB = findViewById(R.id.LeftButton);
        centerB = findViewById(R.id.CenterButton);
        rightB = findViewById(R.id.RightButton);
        textView = findViewById(R.id.promilleErgebnis);


        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            showStartActivity();
        } else {
            refreshData();
            mainButton.setOnClickListener(v -> openCamera());
            leftB.setOnClickListener(v -> openCalculator());
            rightB.setOnClickListener(v -> openStatistics());
        }
    }

    /**
     * Refreshes the values from the main page
     */
    private void refreshData() {
        Runnable runnable = () -> {
            while (running) {
                runOnUiThread(() -> {
                    Backend_Calculation calculate = new Backend_Calculation(this);
                    DecimalFormat f = new DecimalFormat();
                    f.setMaximumFractionDigits(2);
                    try {
                        String promilleString = f.format(calculate.getNormalResultValue());
                        textView.setText(promilleString + " \u2030");
                        textView.setTextSize(50);
                    } catch (Exception e) {
                        textView.setText("Drücke den Button um zu starten!");
                        textView.setTextSize(20);
                    }
                });
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Opens the Calculator page
     */
    public void openCalculator() {
        Intent intent = new Intent(this, Activity_Timeline.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * Opens the Camera Page
     */
    public void openCamera() {

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.blop);
        mp.start();

        Intent intent = new Intent(this, Activity_Camera.class);
        startActivity(intent);

    }

    /**
     * Open the Statistics Page
     */
    public void openStatistics() {
        Intent intent = new Intent(this, Activity_Statistics.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Starts the first Startup activity
     */
    private void showStartActivity() {
        Intent intent = new Intent(this, Activity_FirstStartUp.class);
        startActivity(intent);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

}