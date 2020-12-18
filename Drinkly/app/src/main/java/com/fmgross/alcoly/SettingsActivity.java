package com.fmgross.alcoly;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.fmgross.alcoly.fragments.settingsFragment;


public class SettingsActivity extends AppCompatActivity {
    ImageView rightButton, centerButton, leftButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);


        rightButton = findViewById(R.id.RightButtonSettings);
        centerButton = findViewById(R.id.CenterButtonSettings);
        leftButton = findViewById(R.id.LeftButtonSettings);

        leftButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewCalculator.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        rightButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Statistics.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        centerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new settingsFragment())
                    .commit();
        }
    }
}