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


public class Fragment_NiedrigerWert extends Fragment {

    private TextView timeToDrive, amountOfAlc, promille, textType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_niedriger_wert, container, false);
        timeToDrive = root.findViewById(R.id.textView9Low);
        amountOfAlc = root.findViewById(R.id.amountOfAlcoholLow);
        textType = root.findViewById(R.id.textTypeLow);
        promille = root.findViewById(R.id.PromilleLow);
        refreshData();
        return root;
    }

    private void setData() {
        try {
            Backend_Calculation calculate = new Backend_Calculation(getContext());
            int hours = (int) (calculate.getMinTimeToDrive() / 60);
            int minutes = (int) (calculate.getMinTimeToDrive() % 60);
            String time = hours + ":" + minutes;
            DecimalFormat f = new DecimalFormat();
            f.setMaximumFractionDigits(2);
            int getränke = calculate.getSessionAmount();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    textType.setText(getränke + " alkoholische Getränke");
                    timeToDrive.setText(time + " h");
                    promille.setText(f.format(calculate.getMinResultValue()) + " ‰");
                    amountOfAlc.setText("0");
                });
            }
        } catch (Exception e) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    textType.setText(0 + " alkoholische Getränke");
                    timeToDrive.setText("0 h");
                    promille.setText("0.0 ‰");
                    amountOfAlc.setText("0 ml");
                });
            }
        }
    }

    /**
     * Refreshes the data of the Detailsfragment low on a new thread
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