package com.fmgross.alcoly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.fmgross.alcoly.backend.DatabaseHelper;



public class FirstStartupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_startup);

        Button forwardButton = findViewById(R.id.forwardButton);
        Spinner spinner = findViewById(R.id.spinner);
        EditText ageInput = findViewById(R.id.ageInput);
        EditText weightInput = findViewById(R.id.weightInput);

        generateSaveLocations(spinner);

        forwardButton.setOnClickListener(v -> {

            saveSettings(ageInput, weightInput);

            startMainScreen();
        });
    }

    /**
     * Creates the Shared preference and the database, if it already exists, it cleans it
     *
     * @param spinner the spinner item
     */
    private void generateSaveLocations(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.deleteAllGetraenke();
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

    /**
     * Saves the values inside the SharedPreferences
     *
     * @param ageInput    the age value, from the userInput
     * @param weightInput the weight value, from the userInput
     */
    public void saveSettings(EditText ageInput, EditText weightInput) {


        int age = Integer.parseInt(ageInput.getText().toString());

        String weight = weightInput.getText().toString();

        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("age", age);
        editor.putInt("weight", Integer.parseInt(weight));
        editor.putBoolean("valuesSet", true);
        editor.apply();
    }

    /**
     * Starts the MainScreen
     */
    public void startMainScreen() {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }
}