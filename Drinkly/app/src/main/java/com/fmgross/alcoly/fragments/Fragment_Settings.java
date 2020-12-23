package com.fmgross.alcoly.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.fmgross.alcoly.R;

public class Fragment_Settings extends Fragment {

    private ImageView link, datenschutz;
    private Button male, female, save;
    private EditText age, weight;
    private Switch night, audio, scanOnStart;
    private String gender;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        link = root.findViewById(R.id.formelnButton);
        datenschutz = root.findViewById(R.id.datenschutzButton);
        male = root.findViewById(R.id.maleButton);
        female = root.findViewById(R.id.femaleButton);
        age = root.findViewById(R.id.AlterInput);
        weight = root.findViewById(R.id.GewichtInput);
        night = root.findViewById(R.id.darkmodeSwitch);
        audio = root.findViewById(R.id.audioSwitch);
        scanOnStart = root.findViewById(R.id.startWithScanSwitch);
        save = root.findViewById(R.id.save);
        generateSettings();
        onClickListener();
        return root;
    }

    /**
     * Generates all the OnClickListeners
     */
    private void onClickListener() {
        save.setOnClickListener(v -> {
            SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            int ageValue = Integer.parseInt(age.getText().toString());
            int weightValue = Integer.parseInt(weight.getText().toString());
            editor.putInt("age", ageValue);
            editor.putInt("weight", weightValue);
            editor.putString("gender", gender);
            editor.apply();
        });

        datenschutz.setOnClickListener(v -> {
            Uri uriUrl = Uri.parse("https://fmgross.de/datenschutzerklaerung-von-com-fmgross-alcoly/");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        });

        link.setOnClickListener(v -> {
            Uri uriUrl = Uri.parse("https://fmgross.de/");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        });

        night.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("darkmode", true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("darkmode", false);
            }
            editor.apply();
        });

        audio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("audio", isChecked);
            editor.apply();
        });

        scanOnStart.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("scanOnStart", isChecked);
            editor.apply();
        });

        male.setOnClickListener(v -> {
            gender = "Male";
            male.setBackgroundColor(Color.rgb(255, 205, 25));
            female.setBackgroundColor(Color.rgb(34, 34, 34));
        });

        female.setOnClickListener(v -> {
            gender = "Female";
            male.setBackgroundColor(Color.rgb(34, 34, 34));
            female.setBackgroundColor(Color.rgb(255, 205, 25));
        });
    }

    /**
     * Generates the Fragment with the Values from the SharedPreference "Settings"
     */
    private void generateSettings() {
        SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (settings.getString("gender", "Male").equals("Male")) {
            male.setBackgroundColor(Color.rgb(255, 205, 25));
            female.setBackgroundColor(Color.rgb(34, 34, 34));
            gender = "Male";
        } else {
            male.setBackgroundColor(Color.rgb(34, 34, 34));
            female.setBackgroundColor(Color.rgb(255, 205, 25));
            gender = "Female";
        }
        age.setText(settings.getInt("age", 20) + "");
        weight.setText(settings.getInt("weight", 80) + "");
        audio.setChecked(settings.getBoolean("audio", true));
        night.setChecked(settings.getBoolean("darkmode", true));
        scanOnStart.setChecked(settings.getBoolean("scanOnStart", false));
    }
}
