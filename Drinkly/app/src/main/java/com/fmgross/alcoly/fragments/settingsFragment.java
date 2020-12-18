package com.fmgross.alcoly.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.fmgross.alcoly.R;

public class settingsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private ImageView link, datenschutz;
    private Button male, female;
    private EditText age, weight;
    private Switch night, audio, scanOnStart;

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

        generateSettings();

        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                int weight = Integer.parseInt(s.toString());
                editor.putInt("weight", weight);
            }
        });

        age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                int age = Integer.parseInt(s.toString());
                editor.putInt("age", age);
            }
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
            System.out.println(isChecked);
            SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("darkmode", true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("darkmode", false);
            }
        });

        audio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            System.out.println(isChecked);
            SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("audio", isChecked);
        });

        scanOnStart.setOnCheckedChangeListener((buttonView, isChecked) -> {
            System.out.println(isChecked);
            SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("scanOnStart", isChecked);
        });

        male.setOnClickListener(v -> {
            SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("gender", "Male");
            male.setBackgroundColor(Color.rgb(255, 205, 25));
            female.setBackgroundColor(Color.rgb(34, 34, 34));
        });

        female.setOnClickListener(v -> {
            SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("gender", "Female");
            male.setBackgroundColor(Color.rgb(34, 34, 34));
            female.setBackgroundColor(Color.rgb(255, 205, 25));
        });
        return root;
    }

    private void generateSettings() {
        SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (settings.getString("gender", "Male").equals("Male")) {
            male.setBackgroundColor(Color.rgb(255, 205, 25));
            female.setBackgroundColor(Color.rgb(34, 34, 34));
        } else {
            male.setBackgroundColor(Color.rgb(34, 34, 34));
            female.setBackgroundColor(Color.rgb(255, 205, 25));
        }

        age.setText(settings.getInt("age", 20) + "");
        weight.setText(settings.getInt("weight", 80) + "");
        audio.setChecked(settings.getBoolean("audio", true));
        night.setChecked(settings.getBoolean("darkmode", true));
        scanOnStart.setChecked(settings.getBoolean("scanOnStart", false));

    }
}
