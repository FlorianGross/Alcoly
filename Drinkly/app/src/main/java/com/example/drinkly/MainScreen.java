package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainScreen extends AppCompatActivity {
    Button mainButton, topLeft, topRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mainButton = findViewById(R.id.mainAction);
        topLeft = findViewById(R.id.topLeft);
        topRight = findViewById(R.id.topRight);




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
        topRight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });
    }
    public void openCalculator(){
        Intent intent = new Intent(this, Calculator.class);
        startActivity(intent);
    }
    public void openCamera(){
        Intent intent = new Intent(this, CameraAndKI.class);
        startActivity(intent);
    }
}