package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Calculator extends AppCompatActivity {
    ArrayList<Getränke> drinks;
    double rfact;
    double uDef;
    double promille;
    int age;
    int mass;
    String gender;
    String massString;
    String agePre;
    String promilleFeedback;
    TextView textView;
    RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        textView = findViewById(R.id.textView);
        rv = findViewById(R.id.recyclerView);

        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        startCalculate(editor, settings);
        loadArrayList();

        Adapter myAdapter = new Adapter(this, drinks.get(1).getUri(), drinks.get(1).getDate(), drinks.get(1).getVolume(), drinks.get(1).getVolumePart());
        rv.setAdapter(myAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }
    private void loadArrayList(){
        SharedPreferences sharedPreferences = getSharedPreferences("drinks", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("List", null);
        Type type = new TypeToken<ArrayList<Getränke>>(){}.getType();
        drinks = gson.fromJson(json, type);

        if(drinks == null){
            drinks = new ArrayList<Getränke>();
        }
    }

    public void startCalculate(SharedPreferences.Editor editor, SharedPreferences settings){

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
        System.out.println(mass + " " + rfact + " " + age +  " "  + uDef);
        if(!drinks.isEmpty()) {
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

        for (int i = 0; i < drinks.size(); i++) {
            v = drinks.get(i).getVolume();
            e = drinks.get(i).getVolumePart();
            result = result + (((v * e * p) / (mass * rfact)) - uDef);
        }

        result = result - time * 0.16;


        return result;
    }

}