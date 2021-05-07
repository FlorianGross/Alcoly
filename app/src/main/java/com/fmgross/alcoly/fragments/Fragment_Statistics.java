package com.fmgross.alcoly.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fmgross.alcoly.R;
import com.fmgross.alcoly.backend.Backend_Calculation;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Fragment_Statistics extends Fragment {

    private LineChart lineChart;
    private Button lowValue, mediumValue, highValue;
    private FloatingActionButton settings;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        lineChart = root.findViewById(R.id.lineChart);
        lowValue = root.findViewById(R.id.lowButton);
        mediumValue = root.findViewById(R.id.mediumButton);
        highValue = root.findViewById(R.id.highButton);
        settings = root.findViewById(R.id.settingsButton);

        FragmentManager fm = getActivity().getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment, new Fragment_MittlererWert());
        ft.commit();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initializeBarChart();
            }
        });
        t.start();


        settings.setOnClickListener(v -> {
            FragmentTransaction ft1 = fm.beginTransaction();
            ft1.replace(R.id.nav_host_fragment, new Fragment_Settings());
            ft1.commit();
        });
        lowValue.setOnClickListener(v -> {
            FragmentTransaction ft2 = fm.beginTransaction();
            ft2.replace(R.id.fragment, new Fragment_NiedrigerWert());
            ft2.commit();
        });
        mediumValue.setOnClickListener(v -> {
            FragmentTransaction ft3 = fm.beginTransaction();
            ft3.replace(R.id.fragment, new Fragment_MittlererWert());
            ft3.commit();
        });
        highValue.setOnClickListener(v -> {
            FragmentTransaction ft4 = fm.beginTransaction();
            ft4.replace(R.id.fragment, new Fragment_HoherWert());
            ft4.commit();
        });
        return root;
    }

    /**
     * Initializes the Bar Chart
     */
    private void initializeBarChart() {
        try {
            lineChart.setNoDataText("Graph wird geladen...");
            setChartPreset();
            //setChartPresetTest();

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            Backend_Calculation newCalculator = new Backend_Calculation(getContext());


            /*generateLowValues(dataSets, newCalculator);
            lineChart.setNoDataText("Graph wird geladen.");
            generateMediumValues(dataSets, newCalculator);
            lineChart.setNoDataText("Graph wird geladen..");*/
            generateHighValues(dataSets, newCalculator);
            lineChart.setNoDataText("Graph wird geladen...");

            //generateMedTestValues(dataSets, newCalculator);

            LineData data = new LineData(dataSets);
            try {
                lineChart.getData().setValueTextColor(getResources().getColor(R.color.text, null));
            } catch (NullPointerException g) {
            }
            lineChart.setData(data);
            lineChart.invalidate();
        } catch (Exception e) {
            System.out.println(e);
            lineChart.setNoDataText("Keine Einträge gefunden!");
        }
    }

    private void setChartPreset() {
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setScaleXEnabled(true);
        lineChart.setScaleYEnabled(true);
        lineChart.getXAxis().setEnabled(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.text, null));
        lineChart.getAxisLeft().setEnabled(true);
        lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.text, null));

        // enable touch gestures
        lineChart.setTouchEnabled(false);

        // enable scaling and dragging
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);
        lineChart.getDescription().setEnabled(false);
        Legend l = lineChart.getLegend();
        l.setEnabled(false);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
    }

    private void setChartPresetTest() {
        Legend l = lineChart.getLegend();
        l.setEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                final SimpleDateFormat mFormat = new SimpleDateFormat("HH");
                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(3f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void generateHighValues(ArrayList<ILineDataSet> dataSets, Backend_Calculation newCalculator) {
        ArrayList<Entry> valueshigh = new ArrayList<>();
        valueshigh.add(new Entry(0, 0));
        for (int i = 0; i < newCalculator.getSessionAmount(); i++) {
            valueshigh.add(new Entry(i + 1, newCalculator.getBarHighEntry(i)));
        }
        LineDataSet f = new LineDataSet(valueshigh, "high");
        f.setLineWidth(1f);
        f.setCircleRadius(1.5f);
        f.setValueTextColor(getResources().getColor(R.color.text, null));
        int colorhigh = Color.YELLOW;
        f.setColor(colorhigh);
        f.setCircleColor(colorhigh);
        dataSets.add(f);
    }

    private void generateMediumValues(ArrayList<ILineDataSet> dataSets, Backend_Calculation newCalculator) {
        ArrayList<Entry> valuesmedium = new ArrayList<>();
        valuesmedium.add(new Entry(0, 0));
        for (int i = 0; i < newCalculator.getSessionAmount(); i++) {
            valuesmedium.add(new Entry(i + 1, newCalculator.getBarMediumEntry(i)));
        }
        LineDataSet e = new LineDataSet(valuesmedium, "medium");
        e.setLineWidth(1f);
        e.setCircleRadius(1.5f);
        e.setValueTextColor(getResources().getColor(R.color.text, null));
        int colormedium = Color.RED;
        e.setColor(colormedium);
        e.setCircleColor(colormedium);
        System.out.println(e.toString());
        dataSets.add(e);
    }

    private void generateLowValues(ArrayList<ILineDataSet> dataSets, Backend_Calculation newCalculator) {
        ArrayList<Entry> valueslow = new ArrayList<>();
        valueslow.add(new Entry(0, 0));
        for (int i = 0; i < newCalculator.getSessionAmount(); i++) {
            valueslow.add(new Entry(i + 1, newCalculator.getBarLowEntry(i)));
        }
        LineDataSet d = new LineDataSet(valueslow, "low");
        d.setLineWidth(1f);
        d.setCircleRadius(1.5f);
        d.setValueTextColor(getResources().getColor(R.color.text, null));
        int colorlow = Color.BLUE;
        d.setColor(colorlow);
        d.setCircleColor(colorlow);
        dataSets.add(d);
    }

    private void generateMedTestValues(ArrayList<ILineDataSet> dataSets, Backend_Calculation newCalculator) {
        ArrayList<Entry> valuesTest = new ArrayList<>();
        long now = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
        int beginTime = (int) (now - 12);
        for (int i = beginTime; i <= now; i++) {
            valuesTest.add(new Entry(i, newCalculator.getLineNormalEntryTime((i))));
        }
        System.out.println(valuesTest.toString());
        LineDataSet d = new LineDataSet(valuesTest, "test");
        d.setLineWidth(1f);
        d.setCircleRadius(1.5f);
        d.setValueTextColor(getResources().getColor(R.color.text, null));
        int colorlow = Color.BLUE;
        d.setColor(colorlow);
        d.setCircleColor(colorlow);
        dataSets.add(d);
    }


}