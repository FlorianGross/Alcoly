package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drinkly.backend.DatabaseHelper;
import com.example.drinkly.backend.Getraenke;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Details extends AppCompatActivity {
    private Button edit;
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

        Button back = findViewById(R.id.back);
        edit = findViewById(R.id.edit);
        imageView = findViewById(R.id.currentImage);
        percentage = findViewById(R.id.currentPercentage);
        check1 = findViewById(R.id.currentCheckBox1);
        check2 = findViewById(R.id.currentCheckBox2);
        type = findViewById(R.id.CurrentName);
        currentDate = findViewById(R.id.currentDate);

        generateDelails();

        back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainScreen.class);
            startActivity(intent);
        });
        edit.setOnClickListener(v -> {
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
        });


    }

    /**
     * Generates the details page
     */
    private void generateDelails() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        ArrayList<Getraenke> arrayList = databaseHelper.getAllGetraenke();
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
    }

    /**
     * sets all the values of the details view
     *
     * @param ai the list of all drinks
     * @param i  the position of the current item in the array
     */
    private void setAllValues(ArrayList<Getraenke> ai, int i) {
        if (ai.get(i).getVolume() == 0.5) {
            check1.setChecked(false);
            check2.setChecked(true);
        } else {
            check2.setChecked(false);
            check1.setChecked(true);
        }
        Date newDate = ai.get(i).getDate();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = formatter.format(newDate);
        currentDate.setText(strDate);
        percentage.setText(ai.get(i).getVolumePart() + "\u2030");
        imageView.setImageBitmap(ai.get(i).getUri());
    }
}