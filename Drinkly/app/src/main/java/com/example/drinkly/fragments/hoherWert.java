package com.example.drinkly.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.drinkly.NewCalculator;
import com.example.drinkly.R;

import java.text.DecimalFormat;


public class hoherWert extends Fragment {
    private TextView age, weight, timeToDrive, amountOfAlc, textType, promille;
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
        timeToDrive = root.findViewById(R.id.textView9High);
        amountOfAlc = root.findViewById(R.id.amountOfAlcoholHigh);
        textType = root.findViewById(R.id.textTypeHigh);
        promille = root.findViewById(R.id.PromilleHigh);
        setBasicData();
        refreshData();
        return root;
    }

    private void setBasicData() {
        SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        String genderString = settings.getString("gender", "Male");
        int ageInt = settings.getInt("age", 20);
        int weightInt = settings.getInt("weight", 80);
        age.setText(ageInt + "Jahre");
        weight.setText(weightInt + "kg");
        if(genderImage.equals("Male")){
            genderImage.setImageResource(R.mipmap.male);
        }else{
            genderImage.setImageResource(R.drawable.female);
        }
    }

    private void refreshData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NewCalculator calculate = new NewCalculator();
                            double timeToDriveDouble = calculate.getHighTimeToDrive(getContext().getApplicationContext()) / 60;
                            DecimalFormat f = new DecimalFormat();
                            f.setMaximumFractionDigits(2);
                            String timeToDriveString = f.format(timeToDriveDouble);
                            timeToDrive.setText(timeToDriveString + " h");
                            promille.setText(f.format(calculate.getHighResultValue(getContext().getApplicationContext())) + " ‰");
                            amountOfAlc.setText("0");
                        }
                    });
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}