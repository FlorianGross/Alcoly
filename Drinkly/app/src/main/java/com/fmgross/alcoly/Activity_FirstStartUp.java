package com.fmgross.alcoly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class Activity_FirstStartUp extends AppCompatActivity {
    Button maleImage, femaleImage;
    Button forwardButton, skipButton;
    EditText ageInput, weightInput;
    ConstraintLayout constraintLayout, cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_startup);

        cardView = findViewById(R.id.cardviewStartup);
        constraintLayout = findViewById(R.id.informations);

        forwardButton = findViewById(R.id.forwardButton);
        weightInput = findViewById(R.id.ageInput);
        ageInput = findViewById(R.id.weightInput);
        skipButton = findViewById(R.id.skipButton);

        maleImage = findViewById(R.id.maleImage);
        femaleImage = findViewById(R.id.femaleImage);
        cardView.setVisibility(View.VISIBLE);
        constraintLayout.setVisibility(View.INVISIBLE);

        maleImage.setOnClickListener(v -> {
            maleImage.setBackgroundColor(Color.argb(255, 255, 205, 25));
            femaleImage.setBackgroundColor(Color.argb(255, 99, 99, 99));
            saveGender("Male");
        });
        femaleImage.setOnClickListener(v -> {
            femaleImage.setBackgroundColor(Color.argb(255, 255, 205, 25));
            maleImage.setBackgroundColor(Color.argb(255, 99, 99, 99));
            saveGender("Female");
        });
        skipButton.setOnClickListener(v -> {
            setValues(20, 80);
            forward();
        });
        forwardButton.setOnClickListener(v -> {
            int age = Integer.parseInt(ageInput.getText().toString());

            String weight = weightInput.getText().toString();

            setValues(age, Integer.parseInt(weight));
            forward();
        });
    }

    private void forward() {
        skipButton.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);

        forwardButton.setOnClickListener(v -> {
            startMainScreen();
        });
    }

    /**
     * Saves the other data to the SharedPreference
     *
     * @param age    input from the ageinput
     * @param weight input from the weightinput
     */
    private void setValues(int age, int weight) {
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("age", age);
        editor.putInt("weight", weight);
        editor.putBoolean("valuesSet", true);
        editor.putBoolean("scanOnStart", false);
        editor.putBoolean("darkmode", true);
        editor.putBoolean("audio", true);
        editor.apply();
    }

    /**
     * Saves the gender to the SharedPreference
     *
     * @param gender
     */
    public void saveGender(String gender) {
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("gender", gender);
        editor.apply();
    }

    /**
     * Starts the MainScreen
     */
    public void startMainScreen() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();

        Intent intent = new Intent(this, Activity_MainPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

}