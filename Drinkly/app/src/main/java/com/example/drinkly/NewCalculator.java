package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class NewCalculator extends AppCompatActivity {

    RecyclerView mRecyclerView;

    DatabaseHelper databaseHelper;
    ArrayList<Getränke> arrayList;
    mAdapter arrayAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_calculator);

        mRecyclerView = findViewById(R.id.mRecyclerView);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();
        arrayAdapter = new mAdapter(arrayList);
        mRecyclerView.setAdapter(arrayAdapter);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


    }
}