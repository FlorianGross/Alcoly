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


public class Fragment_MittlererWert extends Fragment {

    private TextView timeToDrive, amountOfAlc, promille, textType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mittlerer_wert, container, false);
        timeToDrive = root.findViewById(R.id.timeToDriveMedium);
        amountOfAlc = root.findViewById(R.id.amountOfAlcoholMedium);
        textType = root.findViewById(R.id.textTypeMedium);
        promille = root.findViewById(R.id.PromilleMedium);
        refreshData();
        return root;
    }

    private void setData() {
        try {
            Backend_Calculation calculate = new Backend_Calculation(getContext());
            int hours = (int) (calculate.getNormalTimeToDrive() / 60);
            int minutes = (int) (calculate.getNormalTimeToDrive() % 60);
            String time = hours + ":" + minutes;
            DecimalFormat f = new DecimalFormat();
            f.setMaximumFractionDigits(2);
            int getränke = calculate.getSessionAmount();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    textType.setText(getränke + " alkoholische Getränke");
                    timeToDrive.setText(time + " h");
                    promille.setText(f.format(calculate.getNormalResultValue()) + " ‰");
                    amountOfAlc.setText("0");
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
     * Refreshes the data of the Detailsfragment medium on a new thread
     */
    private void refreshData() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    setData();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }
}