package com.example.drinkly.oldClass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.drinkly.NonMain.Getränke;
import com.example.drinkly.NonMain.PrefConfig;
import com.example.drinkly.R;
import com.example.drinkly.NonMain.mAdapter;

import java.util.ArrayList;

public class Calculator extends AppCompatActivity {

    ArrayList<Getränke> drinks = new ArrayList<Getränke>();
    private RecyclerView mRecyclerView;
    private com.example.drinkly.NonMain.mAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    double rfact;
    double uDef;
    double promille;
    int age;
    int mass;
    String gender;
    String massString;
    String agePre;
    String promilleFeedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        mRecyclerView = findViewById(R.id.recyclerView);


        drinks = PrefConfig.readListFromPref(this);

        if (drinks == null) {
            drinks = new ArrayList<Getränke>();
            System.out.print("Leeres Array");
        } else {
            System.out.println("Volles Array");
        }

        generateRecyclerView();
    }

    public void generateRecyclerView() {
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new mAdapter(drinks);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        System.out.println("Generated");
    }

    public void startCalculate() {
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        massString = settings.getString("weight", "");
        mass = Integer.parseInt(massString);
        gender = settings.getString("gender", "male");

        agePre = settings.getString("age", "");

        if (gender.equals("male")) {
            rfact = 0.7;
        } else {
            rfact = 0.6;
        }

        age = Integer.parseInt(agePre);

        if (age < 55) {
            uDef = 0.15;
        } else {
            uDef = 0.2;
        }
        System.out.println(mass + " " + rfact + " " + age + " " + uDef);
        if (!drinks.isEmpty()) {
            promille = calculateZ(mass, rfact, age, uDef);

            promilleFeedback = (promille + " ");
            editor.putString("promille", promilleFeedback);
            editor.apply();
        }
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