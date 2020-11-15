package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drinkly.NonMain.DatabaseHelper;
import com.example.drinkly.NonMain.Getränke;

import java.util.ArrayList;

public class Details extends AppCompatActivity {
    ArrayList<Getränke> arrayList;
    DatabaseHelper databaseHelper;
    Button back, edit;
    TextView percentage;
    ImageView imageView;
    CheckBox check1, check2;
    int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        back = findViewById(R.id.back);
        edit = findViewById(R.id.edit);
        imageView = findViewById(R.id.currentImage);
        percentage = findViewById(R.id.currentPercentage);
        check1 = findViewById(R.id.currentCheckBox1);
        check2 = findViewById(R.id.currentCheckBox2);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        current = prefs.getInt("currentNumber", 0);


        setAllValues(arrayList, current);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void setAllValues(ArrayList<Getränke> ai, int i){
        //arrayList.get(current).getUri();
        if(ai.get(i).getVolume() == 0.5){
            check1.setChecked(false);
            check2.setChecked(true);
        }else{
            check2.setChecked(false);
            check1.setChecked(true);
        }
        percentage.setText(ai.get(i).getVolumePart() + "\u2030");
        // imageView.setImageURI(arrayList.get(i).getUri());
    }
}