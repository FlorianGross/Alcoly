package com.fmgross.alcoly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;


public class MainScreen extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ImageView mainButton = findViewById(R.id.mainAction);
        ImageView leftB = findViewById(R.id.LeftButton);
        ImageView centerB = findViewById(R.id.CenterButton);
        ImageView rightB = findViewById(R.id.RightButton);
        textView = findViewById(R.id.promilleErgebnis);


        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            showStartActivity();
        } else {
            refreshData();
            mainButton.setOnClickListener(v -> openCamera());
            leftB.setOnClickListener(v -> openCalculator());
            rightB.setOnClickListener(v -> openNothing());
        }
    }

    private void refreshData() {
        Runnable runnable = () -> {
            while (true) {
                runOnUiThread(() -> {
                    NewCalculator calculate = new NewCalculator();
                    DecimalFormat f = new DecimalFormat();
                    f.setMaximumFractionDigits(2);
                    String promilleString = f.format(calculate.getNormalResultValue(getApplicationContext()));
                    textView.setText(promilleString + " \u2030");
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
        Intent intent = new Intent(this, NewCalculator.class);
        startActivity(intent);
        //overridePendingTransition(android.R.anim.fade_in, 0);
    }

    /**
     * Opens the Camera Page
     */
    public void openCamera() {
        Intent intent = new Intent(this, CameraAndKI.class);
        startActivity(intent);

    }

    /**
     * Open the Statistics Page
     */
    public void openNothing() {
        Intent intent = new Intent(this, Statistics.class);
        startActivity(intent);
    }

    /**
     * Starts the first Startup activity
     */
    private void showStartActivity() {
        Intent intent = new Intent(this, FirstStartupActivity.class);
        startActivity(intent);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();

    }
}