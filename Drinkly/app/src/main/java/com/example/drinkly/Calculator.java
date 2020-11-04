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
        double rfact;
        if (gender.equals("male")) {
            rfact = 0.7;
        } else {
            rfact = 0.6;
        }
        String agePre = settings.getString("age", "");
        int age = Integer.parseInt(agePre);
        double uDef;
        if (age < 55) {
            uDef = 0.15;
        } else {
            uDef = 0.2;
        }
        double promille = calculateZ(mass, rfact, age, uDef);
        SharedPreferences.Editor editor = settings.edit();
        String promilleFeedback = "" + promille;
        editor.putString("promille", promilleFeedback);
        editor.apply();
    }

    public double calculateZ(int mass, double rfact, int time, double uDef) {
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