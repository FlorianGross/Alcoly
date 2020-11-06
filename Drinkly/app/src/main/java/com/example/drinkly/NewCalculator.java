package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class NewCalculator extends AppCompatActivity {

    ListView listView;

    DatabaseHelper databaseHelper;
    ArrayList<Getränke> arrayList;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_calculator);

        listView = findViewById(R.id.listView);

        databaseHelper = new DatabaseHelper(NewCalculator.this);
        arrayList = databaseHelper.getAllGetraenke();
        arrayAdapter = new ArrayAdapter<Getränke>(NewCalculator.this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);

    }
}