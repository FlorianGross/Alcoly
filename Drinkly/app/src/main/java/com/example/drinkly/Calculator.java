package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class Calculator extends AppCompatActivity {

    ArrayList<Getränke> drinks;
    private TextView textView;
    private RecyclerView rv;
    private Adapter mAdapter;
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

        textView = findViewById(R.id.textView);
        rv = findViewById(R.id.recyclerView);


        loadArrayList();
        if(!(drinks == null)) {
            loadRecycleView();
        }else{
            System.out.println("Error");
        }
        // startCalculate();

    }

    private void loadArrayList() {
        SharedPreferences sharedPreferences = getSharedPreferences("drinks", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("List", null);
        Type type = new TypeToken<ArrayList<Getränke>>() {
        }.getType();
        drinks = gson.fromJson(json, type);
        if (drinks == null) {
            drinks = new ArrayList<Getränke>();
        }

    }

    private void loadRecycleView() {
        mAdapter = new Adapter(drinks);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);

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
            textView.setText("promille: " + promilleFeedback);
        }
    }

    public double calculateZ(int mass, double rfact, int time, double uDef) {
        double p = 0.8;
        double v;
        double e;
        double result = 0;
/*
        for (int i = 0; i < drinks.size(); i++) {
            v = drinks.get(i).getVolume();
            e = drinks.get(i).getVolumePart();
            result = result + (((v * e * p) / (mass * rfact)) - uDef);
        }

        result = result - time * 0.16;

*/
        return result;
    }

}