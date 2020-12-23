package com.fmgross.alcoly;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fmgross.alcoly.fragments.Fragment_Fragment_Settings;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new Fragment_Fragment_Settings())
                    .commit();
        }
    }
}