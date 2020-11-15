package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;


public class FirstStartupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_startup);

        Button forwardButton = findViewById(R.id.forwardButton);
        Spinner spinner = findViewById(R.id.spinner);

        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        EditText ageInput = findViewById(R.id.ageInput);
        EditText weightInput = findViewById(R.id.weightInput);

        forwardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    saveSettings(ageInput, weightInput);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                startMainScreen();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String gender = parent.getItemAtPosition(position).toString();
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("gender", gender);
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void saveSettings(EditText ageInput, EditText weightInput) throws ParseException {


        int age = Integer.parseInt(ageInput.getText().toString());

        String weight = weightInput.getText().toString();

        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("age", age);
        editor.putString("weight", weight);
        editor.apply();
    }

    public void startMainScreen() {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }
}