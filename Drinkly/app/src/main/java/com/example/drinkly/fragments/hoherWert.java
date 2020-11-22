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

import com.example.drinkly.NewCalculator;
import com.example.drinkly.R;


public class hoherWert extends Fragment {
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



        refreshData();
        return root;
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
                            double returnDouble = calculate.getHighTimeToDrive(getContext().getApplicationContext()) / 60;
                            String returnString = String.format("%s", returnDouble);
                            timeToDrive.setText(returnString + " h");

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