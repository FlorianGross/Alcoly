package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.drinkly.NonMain.DatabaseHelper;
import com.example.drinkly.NonMain.Getränke;
import com.example.drinkly.NonMain.GroupAdapter;
import com.example.drinkly.NonMain.myAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class NewCalculator extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ArrayList<Getränke> arrayList;
    private myAdapter adapter;
    private GroupAdapter groupAdapter;
    private ArrayList<String> arrayListString;
    private myAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_calculator);

        /*
        databaseHelper = new DatabaseHelper(getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();

        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        int numberOfColumns = 3;

        setOnCLickListener();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new myAdapter(this, arrayList, listener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(false);
*/
        databaseHelper = new DatabaseHelper(getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();
        arrayListString = databaseHelper.getAllDates(arrayList);
        RecyclerView newRecyclerView = findViewById(R.id.mRecyclerView);
        newRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupAdapter = new GroupAdapter(this, arrayListString);
        newRecyclerView.setAdapter(groupAdapter);
        newRecyclerView.setHasFixedSize(false);

    }

    private void setOnCLickListener() {
        listener = new myAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), Details.class);
                intent.putExtra("intPosition", position);
                startActivity(intent);
            }
        };
    }

}

