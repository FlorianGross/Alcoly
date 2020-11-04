package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainScreen extends AppCompatActivity {
    Button topLeft, topRight;
    ImageView mainButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mainButton = findViewById(R.id.mainAction);
        topLeft = findViewById(R.id.topLeft);
        topRight = findViewById(R.id.topRight);
        textView = findViewById(R.id.promilleErgebnis);
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        String promille = settings.getString("promille", "");
        if(promille != null) {
            textView.setText(promille + " Promille");
        }else{
            textView.setText("Alkoli");
        }

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalculator();
            }
        });

        topRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openNothing();
            }
        });
    }

    public void openCalculator() {
        Intent intent = new Intent(this, Calculator.class);
        startActivity(intent);
        //overridePendingTransition(android.R.anim.fade_in, 0);
    }

    public void openCamera() {
        Intent intent = new Intent(this, CameraAndKI.class);
        startActivity(intent);

    }
    public void openNothing(){
        Intent intent = new Intent(this, Statistics.class);
        startActivity(intent);
        //overridePendingTransition(android.R.anim.fade_in, 0);
    }
}