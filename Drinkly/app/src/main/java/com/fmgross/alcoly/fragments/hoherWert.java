package com.fmgross.alcoly.fragments;


import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fmgross.alcoly.NewCalculator;
import com.fmgross.alcoly.R;

import java.text.DecimalFormat;


public class hoherWert extends Fragment {

    private TextView timeToDrive, amountOfAlc, promille, textType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hoher_wert, container, false);
        timeToDrive = root.findViewById(R.id.textView9High);
        amountOfAlc = root.findViewById(R.id.amountOfAlcoholHigh);
        textType = root.findViewById(R.id.textTypeHigh);
        promille = root.findViewById(R.id.PromilleHigh);
        refreshData();
        return root;
    }


    private void refreshData() {
        Runnable runnable = () -> {
            while (true) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            NewCalculator calculate = new NewCalculator();
                            int hours = (int) (calculate.getMinTimeToDrive(getContext().getApplicationContext()) / 60);
                            int minutes = (int) (calculate.getMinTimeToDrive(getContext().getApplicationContext()) % 60);
                            String time = hours + ":" + minutes;
                            DecimalFormat f = new DecimalFormat();
                            f.setMaximumFractionDigits(2);
                            int getränke = calculate.getSessionAmount(this.getActivity());
                            textType.setText(getränke + " alkoholische Getränke");
                            timeToDrive.setText(time + " h");
                            promille.setText(f.format(calculate.getHighResultValue(getContext().getApplicationContext())) + " ‰");
                            amountOfAlc.setText("0");
                        } catch (Exception e) {
                            timeToDrive.setText("0 h");
                            promille.setText("0.0 ‰");
                            amountOfAlc.setText("0 ml");
                            textType.setText(0 + " alkoholische Getränke");
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