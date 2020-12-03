package com.fmgross.alcoly;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmgross.alcoly.fragments.hoherWert;
import com.fmgross.alcoly.fragments.mittlererWert;
import com.fmgross.alcoly.fragments.niedrigerWert;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Statistics extends AppCompatActivity {
    private BarChart barChart;
    final int[] colorClassArray = new int[]{Color.BLUE, Color.WHITE, Color.RED};
    private Button lowValue, mediumValue, highValue, edit;
    private ImageView genderImage;
    private TextView age, weight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        edit = findViewById(R.id.editButtonMedium);
        barChart = findViewById(R.id.barChart);
        lowValue = findViewById(R.id.lowButton);
        mediumValue = findViewById(R.id.mediumButton);
        highValue = findViewById(R.id.highButton);
        age = findViewById(R.id.ageTextStatsMedium);
        weight = findViewById(R.id.weightTextStatsMedium);
        genderImage = findViewById(R.id.imageView8Medium);

        setBasicData();

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment, new mittlererWert());
        ft.commit();

        initializeBarChart();

        edit.setOnClickListener(v -> {
            Intent intent = new Intent(this, FirstStartupActivity.class);
            startActivity(intent);
        });
        lowValue.setOnClickListener(v -> {
            FragmentTransaction ft1 = fm.beginTransaction();
            ft1.replace(R.id.fragment, new niedrigerWert());
            ft1.commit();
        });
        mediumValue.setOnClickListener(v -> {
            FragmentTransaction ft12 = fm.beginTransaction();
            ft12.replace(R.id.fragment, new mittlererWert());
            ft12.commit();
        });
        highValue.setOnClickListener(v -> {
            FragmentTransaction ft13 = fm.beginTransaction();
            ft13.replace(R.id.fragment, new hoherWert());
            ft13.commit();
        });

    }

    private void setBasicData() {
        SharedPreferences settings = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String genderString = settings.getString("gender", "Male");
        int ageInt = settings.getInt("age", 20);
        int weightInt = settings.getInt("weight", 80);
        age.setText(ageInt + "Jahre");
        weight.setText(weightInt + "kg");
        if (genderString.equals("Male")) {
            genderImage.setImageResource(R.mipmap.male);
        } else {
            genderImage.setImageResource(R.drawable.female);
        }
    }

    private void initializeBarChart() {
        ArrayList<BarEntry> drink = new ArrayList<>();
        //drink = addDrinks();
        //drink.add(new BarEntry(20f, new float[]{1.0f, 1.2f, 1.3f}));
        BarDataSet barDataSet = new BarDataSet(drink, "Getränke");
        barDataSet.setColors(colorClassArray);
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setValueTextSize(16f);
        BarData barData = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.animateY(500);

    }

    private ArrayList<BarEntry> addDrinks() {
        NewCalculator newCalculator = new NewCalculator();
        ArrayList<BarEntry> drinks = new ArrayList<>();
        ArrayList<Integer> dateList = newCalculator.getDates();
        for (int i = 0; i < dateList.size(); i++) {
            Date newDate = new Date(dateList.get(i));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
            String date = simpleDateFormat.format(newDate);
            // drinks.add(new BarEntry(Float.parseFloat(date), new float[]{newCalculator.getMinPermilAtTime(this, dateList.get(i)),newCalculator.getMedPermilAtTime(this, dateList.get(i)),newCalculator.getMaxPermilAtTime(this, dateList.get(i))}));
        }
        return drinks;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}