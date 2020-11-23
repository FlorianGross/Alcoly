package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.drinkly.fragments.hoherWert;
import com.example.drinkly.fragments.mittlererWert;
import com.example.drinkly.fragments.niedrigerWert;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;


public class Statistics extends AppCompatActivity {
    BarChart barChart;
    final int[] colorClassArray = new int[]{Color.BLUE, Color.WHITE, Color.RED};
    Button lowValue, mediumValue, highValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        barChart = findViewById(R.id.barChart);
        lowValue = findViewById(R.id.lowButton);
        mediumValue = findViewById(R.id.mediumButton);
        highValue = findViewById(R.id.highButton);

        FragmentManager fm = getSupportFragmentManager();


        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment, new hoherWert());
        ft.commit();


        initializeBarChart();


        lowValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment, new niedrigerWert());
                ft.commit();
            }
        });
        mediumValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment, new mittlererWert());
                ft.commit();
            }
        });
        highValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment, new hoherWert());
                ft.commit();
            }
        });

    }

    private void initializeBarChart() {
        ArrayList<BarEntry> drink = new ArrayList<>();
        //drink = addDrinks();
        drink.add(new BarEntry(20f, new float[]{1.0f, 1.2f, 1.3f}));
        BarDataSet barDataSet = new BarDataSet(drink, "Getränke");
        barDataSet.setColors(colorClassArray);
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setValueTextSize(16f);
        BarData barData = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.animateY(500);

    }
/*
    private ArrayList<BarEntry> addDrinks() {
        NewCalculator newCalculator = new NewCalculator();
        ArrayList<BarEntry> drinks = new ArrayList<>();
        ArrayList<Long> dateList = newCalculator.getDates();
        for (int i = 0; i < dateList.size(); i++) {
            Date newDate = new Date(dateList.get(i));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
            String date = simpleDateFormat.format(newDate);
            drinks.add(new BarEntry(Float.parseFloat(date), new float[]{newCalculator.getMinPermilAtTime(dateList.get(i)),newCalculator.getMedPermilAtTime(dateList.get(i)),newCalculator.getMaxPermilAtTime(dateList.get(i))}));
        }
        return drinks;
    }
*/
}