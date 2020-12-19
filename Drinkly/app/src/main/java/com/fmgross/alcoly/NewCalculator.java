package com.fmgross.alcoly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import com.fmgross.alcoly.backend.DatabaseHelper;
import com.fmgross.alcoly.backend.Getraenke;
import com.fmgross.alcoly.backend.GroupAdapter;
import com.fmgross.alcoly.backend.myAdapter;

import java.util.ArrayList;
import java.util.Date;

public class NewCalculator extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ArrayList<Getraenke> arrayList;
    private ArrayList<Integer> arrayListString;
    public double minResult;
    public double normalResult;
    public double highResult;
    private ImageView left, right, center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_calculator);
        left = findViewById(R.id.LeftButtonCalc);
        center = findViewById(R.id.CenterButtonCalc);
        right = findViewById(R.id.RightButtonCalc);


        getDatabase();
        createRecycler();


        left.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewCalculator.class);
            startActivity(intent);
        });
        right.setOnClickListener(v -> {
            Intent intent = new Intent(this, Statistics.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        center.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    /**
     * Returns all getränke inside an arrayList
     */
    private void getDatabase() {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();
        arrayListString = databaseHelper.getAllDates();
    }

    /**
     * Creates the recyclerview
     */
    private void createRecycler() {
        RecyclerView newRecyclerView = findViewById(R.id.mRecyclerView);
        newRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GroupAdapter groupAdapter = new GroupAdapter(this, arrayListString);
        newRecyclerView.setAdapter(groupAdapter);
        newRecyclerView.setHasFixedSize(false);
    }

    private void setOnCLickListener() {
        myAdapter.RecyclerViewClickListener listener = (v, position) -> {
            Intent intent = new Intent(getApplicationContext(), Details.class);
            intent.putExtra("intPosition", position);
            startActivity(intent);
        };
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}

