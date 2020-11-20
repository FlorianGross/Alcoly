package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Statistics extends AppCompatActivity {
    Button niedrigerWert, mittlererWert, hoherWert;
    BarChart barChart;
    int[] colorClassArray = new int[]{0x3B7FCE, Color.WHITE, 0xFF852A};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        niedrigerWert = findViewById(R.id.niedrigerWert);
        mittlererWert = findViewById(R.id.mittlererWert);
        hoherWert = findViewById(R.id.hoherWert);
        barChart = findViewById(R.id.barChart);

        initializeBarChart();


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

    private void initializeBarChart() {
        ArrayList<BarEntry> drink = new ArrayList<>();
        drink = addDrinks();
        BarDataSet barDataSet = new BarDataSet(drink, "Getränke");
        barDataSet.setColors(colorClassArray);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        BarData barData = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.animateY(500);

    }

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

}