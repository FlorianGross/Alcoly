package com.example.drinkly.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drinkly.R;


public class hoherWert extends Fragment {
    private hoherWertViewModel hoherWertViewModel;
    private TextView age, weight, timeToDrive, amountOfAlc, textType;
    private ImageView genderImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hoher_wert, container, false);
        age = root.findViewById(R.id.ageTextStatsHigh);
        weight = root.findViewById(R.id.weightTextStatsHigh);
        genderImage = root.findViewById(R.id.imageView8);
        timeToDrive = root.findViewById(R.id.textView9);
        amountOfAlc = root.findViewById(R.id.amountOfAlcoholHigh);
        textType = root.findViewById(R.id.textTypeHigh);

        hoherWertViewModel =
                new ViewModelProvider(this).get(hoherWertViewModel.class);

        hoherWertViewModel.getAmountOfAlc().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                amountOfAlc.setText(s);
            }
        });
        hoherWertViewModel.getTimeToDrive().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                timeToDrive.setText(s);
            }
        });
        hoherWertViewModel.getTextType().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textType.setText(s);
            }
        });


        return root;
    }
}