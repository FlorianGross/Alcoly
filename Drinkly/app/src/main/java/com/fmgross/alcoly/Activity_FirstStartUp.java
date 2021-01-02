package com.fmgross.alcoly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class Activity_FirstStartUp extends AppCompatActivity {
    Button maleImage, femaleImage;
    Button forwardButton, skipButton;
    EditText ageInput, weightInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_startup);

        forwardButton = findViewById(R.id.forwardButton);
        ageInput = findViewById(R.id.ageInput);
        weightInput = findViewById(R.id.weightInput);
        skipButton = findViewById(R.id.skipButton);

        maleImage = findViewById(R.id.maleImage);
        femaleImage = findViewById(R.id.femaleImage);


        maleImage.setOnClickListener(v -> {
            maleImage.setBackgroundResource(R.mipmap.orangerectangle);
            femaleImage.setBackgroundResource(R.mipmap.blackrectangle);
            saveGender("Male");
        });
        femaleImage.setOnClickListener(v -> {
            femaleImage.setBackgroundResource(R.mipmap.orangerectangle);
            maleImage.setBackgroundResource(R.mipmap.blackrectangle);
            saveGender("Female");
        });
        skipButton.setOnClickListener(v -> {
            setValues(20, 80);
            startMainScreen();
        });
        forwardButton.setOnClickListener(v -> {
            int age = Integer.parseInt(ageInput.getText().toString());

            String weight = weightInput.getText().toString();

            setValues(age, Integer.parseInt(weight));
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