package com.fmgross.alcoly.old;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.fmgross.alcoly.R;
import com.fmgross.alcoly.backend.Backend_DatabaseHelper;
import com.fmgross.alcoly.backend.Backend_Getraenk;
import com.fmgross.alcoly.backend.Backend_GroupAdapter;
import com.fmgross.alcoly.backend.Backend_Adapter;

import java.util.ArrayList;

public class Activity_Timeline extends AppCompatActivity {
    private Backend_DatabaseHelper databaseHelper;
    private ArrayList<Backend_Getraenk> arrayList;
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
            Intent intent = new Intent(this, Activity_Timeline.class);
            startActivity(intent);
        });
        right.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Statistics.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        center.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_MainScreen.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    /**
     * Returns all getränke inside an arrayList
     */
    private void getDatabase() {
        databaseHelper = new Backend_DatabaseHelper(getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();
        arrayListString = databaseHelper.getAllDates();
    }

    /**
     * Creates the recyclerview
     */
    private void createRecycler() {
        RecyclerView newRecyclerView = findViewById(R.id.mRecyclerView);
        newRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Backend_GroupAdapter groupAdapter = new Backend_GroupAdapter(this, arrayListString);
        newRecyclerView.setAdapter(groupAdapter);
        newRecyclerView.setHasFixedSize(false);
    }

    private void setOnCLickListener() {
        Backend_Adapter.RecyclerViewClickListener listener = (v, position) -> {
            Intent intent = new Intent(getApplicationContext(), Activity_Details.class);
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

