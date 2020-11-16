package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainScreen extends AppCompatActivity {
    private ImageView mainButton;
    private TextView textView;
    private Button leftB, centerB, rightB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mainButton = findViewById(R.id.mainAction);
        textView = findViewById(R.id.promilleErgebnis);
        leftB = findViewById(R.id.LeftButton);
        centerB = findViewById(R.id.CenterButton);
        rightB = findViewById(R.id.RightButton);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            showStartActivity();
        } else {


            SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
            String promille = settings.getString("promille", "");
            if (promille != null) {
                textView.setText(promille + " Promille");
            } else {
                textView.setText("Alkoli");
            }

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

    public void openCalculator() {
        Intent intent = new Intent(this, NewCalculator.class);
        startActivity(intent);
        //overridePendingTransition(android.R.anim.fade_in, 0);
    }

    public void openCamera() {
        Intent intent = new Intent(this, CameraAndKI.class);
        startActivity(intent);

    }

    public void openNothing() {
        Intent intent = new Intent(this, Statistics.class);
        startActivity(intent);
        //overridePendingTransition(android.R.anim.fade_in, 0);
    }

    private void showStartActivity() {
        Intent intent = new Intent(this, FirstStartupActivity.class);
        startActivity(intent);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();

    }
}