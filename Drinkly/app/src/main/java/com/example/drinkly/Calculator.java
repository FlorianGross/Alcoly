package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;

public class Calculator extends AppCompatActivity {
    ArrayList<Getränke> drinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        String massString = settings.getString("weight", "");
        int mass = Integer.parseInt(massString);
        String gender = settings.getString("gender", "male");
        if (gender.equals("male")) {
            double rfact = 0.7;
        } else {
            double rfact = 0.6;
        }
        String agePre = settings.getString("age", "");
        int age = Integer.parseInt(agePre);
        if (age < 55) {
            double uDef = 0.15;
        } else {
            double uDef = 0.2;
        }
    }

    public double calculateZ(int mass, double rfact, int time, float uDef) {
        double p = 0.8;
        double v;
        double e;
        double result = 0;

        for (int i = 0; i < drinks.size(); i++) {
            v = drinks.get(i).getVolume();
            e = drinks.get(i).getVolumePart();
            result = result + (((v * e * p) / (mass * rfact)) - uDef);
        }

        result = result - time * 0.16;


        return result;
    }
}