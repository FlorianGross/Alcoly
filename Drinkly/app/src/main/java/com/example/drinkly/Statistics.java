package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Statistics extends AppCompatActivity {
    Button niedrigerWert, mittlererWert, hoherWert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        niedrigerWert = findViewById(R.id.niedrigerWert);
        mittlererWert = findViewById(R.id.mittlererWert);
        hoherWert = findViewById(R.id.hoherWert);





        niedrigerWert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mittlererWert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        hoherWert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}