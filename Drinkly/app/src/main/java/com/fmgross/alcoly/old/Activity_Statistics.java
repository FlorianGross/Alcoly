package com.fmgross.alcoly.old;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.fmgross.alcoly.R;
import com.fmgross.alcoly.backend.Backend_Calculation;
import com.fmgross.alcoly.fragments.Fragment_HoherWert;
import com.fmgross.alcoly.fragments.Fragment_MittlererWert;
import com.fmgross.alcoly.fragments.Fragment_NiedrigerWert;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class Activity_Statistics extends AppCompatActivity {
    private BarChart barChart;
    final int[] colorClassArray = new int[]{Color.BLUE, Color.WHITE, Color.RED};
    private Button lowValue, mediumValue, highValue;
    private ImageView rightButton, centerButton, leftButton;
    private FloatingActionButton settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        barChart = findViewById(R.id.barChart);
        lowValue = findViewById(R.id.lowButton);
        mediumValue = findViewById(R.id.mediumButton);
        highValue = findViewById(R.id.highButton);
        rightButton = findViewById(R.id.RightButtonStats);
        centerButton = findViewById(R.id.CenterButtonStats);
        leftButton = findViewById(R.id.LeftButtonStats);
        settings = findViewById(R.id.settingsButton);

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment, new Fragment_MittlererWert());
        ft.commit();

        initializeBarChart();

        settings.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Settings.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        leftButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Timeline.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        rightButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Statistics.class);
            startActivity(intent);
        });
        centerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_MainScreen.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        lowValue.setOnClickListener(v -> {
            FragmentTransaction ft1 = fm.beginTransaction();
            ft1.replace(R.id.fragment, new Fragment_NiedrigerWert());
            ft1.commit();
        });
        mediumValue.setOnClickListener(v -> {
            FragmentTransaction ft12 = fm.beginTransaction();
            ft12.replace(R.id.fragment, new Fragment_MittlererWert());
            ft12.commit();
        });
        highValue.setOnClickListener(v -> {
            FragmentTransaction ft13 = fm.beginTransaction();
            ft13.replace(R.id.fragment, new Fragment_HoherWert());
            ft13.commit();
        });

    }

    /**
     * Initializes the Bar Chart
     */
    private void initializeBarChart() {
        ArrayList<BarEntry> drink = new ArrayList<>();
        drink.add(0, new BarEntry(0, 0));
        //drink.add(new BarEntry(20f, new float[]{1.0f, 1.2f, 1.3f}));
        BarDataSet barDataSet = new BarDataSet(drink, "Getränke");
        barDataSet.setColors(colorClassArray);
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setValueTextSize(16f);
        BarData barData = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setDragXEnabled(true);
        barChart.setDragYEnabled(false);
        barChart.setData(barData);
        barChart.setDrawGridBackground(false);


        barChart.animateY(500);
    }

    /**
     * Adds Entries to the Bar chart
     *
     * @return all the bar entries
     */
    private ArrayList<BarEntry> addDrinks() {
        Backend_Calculation newCalculator = new Backend_Calculation(this);
        ArrayList<BarEntry> drinks = new ArrayList<>();
        ArrayList<Integer> dateList = newCalculator.getDates();
        for (int i = 0; i < dateList.size(); i++) {
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