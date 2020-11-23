package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drinkly.NonMain.Calculate;
import com.example.drinkly.NonMain.DatabaseHelper;
import com.example.drinkly.NonMain.Getränke;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class MainScreen extends AppCompatActivity {
    private ImageView mainButton;
    private TextView textView;
    private ImageView leftB, centerB, rightB;

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

        if (firstStart || prefs.getInt("weight", -1) == -1) {
            showStartActivity();
        } else {
            refreshData();
            mainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCamera();
                }
            });
            leftB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCalculator();
                }
            });
            rightB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openNothing();
                }
            });
        }
    }

    private void refreshData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NewCalculator calculate = new NewCalculator();
                            DecimalFormat f = new DecimalFormat();
                            f.setMaximumFractionDigits(2);
                            String promilleString = f.format(calculate.getNormalResultValue(getApplicationContext()));
                            textView.setText(promilleString + " \u2030");
                        }
                    });
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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