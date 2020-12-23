package com.fmgross.alcoly.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fmgross.alcoly.R;
import com.fmgross.alcoly.backend.Backend_Calculation;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Fragment_Fragment_Statistics extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private BarChart barChart;
    final int[] colorClassArray = new int[]{Color.BLUE, Color.WHITE, Color.RED};
    private Button lowValue, mediumValue, highValue;
    private FloatingActionButton settings;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fragment_statistics, container, false);

        barChart = root.findViewById(R.id.barChart);
        lowValue = root.findViewById(R.id.lowButton);
        mediumValue = root.findViewById(R.id.mediumButton);
        highValue = root.findViewById(R.id.highButton);
        settings = root.findViewById(R.id.settingsButton);

        FragmentManager fm = getActivity().getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment, new Fragment_mittlerer_wert());
        ft.commit();

        initializeBarChart();

        settings.setOnClickListener(v -> {

            ft.add(R.id.nav_host_fragment, new Fragment_Fragment_Settings());
            ft.commit();

        });
        lowValue.setOnClickListener(v -> {
            ft.replace(R.id.fragment, new Fragment_niedriger_wert());
            ft.commit();
        });
        mediumValue.setOnClickListener(v -> {
            ft.replace(R.id.fragment, new Fragment_mittlerer_wert());
            ft.commit();
        });
        highValue.setOnClickListener(v -> {
            ft.replace(R.id.fragment, new Fragment_hoher_wert());
            ft.commit();
        });



        return root;
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
        Backend_Calculation newCalculator = new Backend_Calculation(getContext());
        ArrayList<BarEntry> drinks = new ArrayList<>();
        ArrayList<Integer> dateList = newCalculator.getDates();
        for (int i = 0; i < dateList.size(); i++) {
            // drinks.add(new BarEntry(Float.parseFloat(date), new float[]{newCalculator.getMinPermilAtTime(this, dateList.get(i)),newCalculator.getMedPermilAtTime(this, dateList.get(i)),newCalculator.getMaxPermilAtTime(this, dateList.get(i))}));
        }
        return drinks;
    }

}
