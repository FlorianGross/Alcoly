package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drinkly.NonMain.DatabaseHelper;
import com.example.drinkly.NonMain.Getränke;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Details extends AppCompatActivity {
    private ArrayList<Getränke> arrayList;
    private DatabaseHelper databaseHelper;
    private Button back, edit;
    private EditText percentage, type;
    private TextView currentDate;
    private ImageView imageView;
    private CheckBox check1, check2;
    private int current = 0;
    private Boolean saveMode = false;

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
        type = findViewById(R.id.CurrentName);
        currentDate = findViewById(R.id.currentDate);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            current = extras.getInt("intPosition");
        }
        percentage.setFocusable(false);
        type.setFocusable(false);
        percentage.setFocusableInTouchMode(false);
        type.setFocusableInTouchMode(false);
        if (saveMode = false) {
            edit.setText("Edit");
        }
        setAllValues(arrayList, current);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(intent);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!saveMode) {
                    percentage.setFocusable(true);
                    type.setFocusable(true);
                    percentage.setFocusableInTouchMode(true);
                    type.setFocusableInTouchMode(true);
                    edit.setText("Save");
                    saveMode = true;
                } else {
                    percentage.setFocusable(false);
                    type.setFocusable(false);
                    percentage.setFocusableInTouchMode(false);
                    type.setFocusableInTouchMode(false);
                    edit.setText("Edit");
                    saveMode = false;
                }
            }
        });


    }

    private void setAllValues(ArrayList<Getränke> ai, int i) {
        //arrayList.get(current).getUri();
        if (ai.get(i).getVolume() == 0.5) {
            check1.setChecked(false);
            check2.setChecked(true);
        } else {
            check2.setChecked(false);
            check1.setChecked(true);
        }
        Date newDate = new Date(ai.get(i).getDate());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = formatter.format(newDate);
        currentDate.setText(strDate);
        percentage.setText(ai.get(i).getVolumePart() * 1000 + "\u2030");
        imageView.setImageBitmap(ai.get(i).getUri());
    }
}