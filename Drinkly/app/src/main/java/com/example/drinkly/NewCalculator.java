package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.drinkly.NonMain.DatabaseHelper;
import com.example.drinkly.NonMain.Getränke;
import com.example.drinkly.NonMain.mAdapter;
import com.example.drinkly.NonMain.myAdapter;

import java.util.ArrayList;

public class NewCalculator extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    ArrayList<Getränke> arrayList;
    myAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_calculator);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();

        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        int numberOfColumns = 3;

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new myAdapter(this, arrayList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(false);



    }

}

