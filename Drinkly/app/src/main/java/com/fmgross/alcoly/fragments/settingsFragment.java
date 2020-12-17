package com.fmgross.alcoly.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

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

         link = root.findViewById(R.id.datenschutzButton);
         datenschutz = root.findViewById(R.id.);
         male = root.findViewById(R.id.);
         female = root.findViewById(R.id.);
         age = root.findViewById(R.id.);
         weight = root.findViewById(R.id.);
         night = root.findViewById(R.id.);
         audio = root.findViewById(R.id.);
         scanOnStart = root.findViewById(R.id.);




        return root;

    }
}
