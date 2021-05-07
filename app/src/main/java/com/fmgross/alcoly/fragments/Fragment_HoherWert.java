package com.fmgross.alcoly.fragments;


import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fmgross.alcoly.R;
import com.fmgross.alcoly.backend.Backend_Calculation;

import java.text.DecimalFormat;


public class Fragment_HoherWert extends Fragment {

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

    private void setData() {
        try {
            Backend_Calculation calculate = new Backend_Calculation(getContext());
            int hours = (int) (calculate.getHighTimeToDrive() / 60);
            int minutes = (int) (calculate.getHighTimeToDrive() % 60);
            String time = hours + ":" + minutes;
            DecimalFormat f = new DecimalFormat();
            f.setMaximumFractionDigits(2);
            int getränke = calculate.getSessionAmount();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    textType.setText(getränke + " alkoholische Getränke");
                    timeToDrive.setText(time + " h");
                    promille.setText(f.format(calculate.getHighResultValue()) + " ‰");
                    amountOfAlc.setText(f.format(calculate.getAmountOfAlcResult()) + " g");
                });
            }
        } catch (Exception e) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    timeToDrive.setText("0 h");
                    promille.setText("0.0 ‰");
                    amountOfAlc.setText("0 ml");
                    textType.setText(0 + " alkoholische Getränke");
                });
            }
        }
    }

    /**
     * Refreshes the data of the Detailsfragment high on a new thread
     */
    private void refreshData() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                setData();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}