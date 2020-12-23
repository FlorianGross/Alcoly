package com.fmgross.alcoly.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import com.fmgross.alcoly.CameraAndKI;
import com.fmgross.alcoly.FirstStartupActivity;
import com.fmgross.alcoly.R;
import com.fmgross.alcoly.backend.Calculation;

import java.text.DecimalFormat;

public class Fragment_Main extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        refreshData();
        mainButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CameraAndKI.class);
            startActivity(intent);
        });
    }

    private TextView textView;
    private final boolean running = true;
    private ImageView mainButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fragment_main, container, false);
        mainButton = root.findViewById(R.id.mainAction);
        textView = root.findViewById(R.id.promilleErgebnis);
        return root;
    }

    /**
     * Refreshes the values from the main page
     */
    private void refreshData() {
        Runnable runnable = () -> {
            while (running) {
                getActivity().runOnUiThread(() -> {
                    Calculation calculate = new Calculation(getContext());
                    DecimalFormat f = new DecimalFormat();
                    f.setMaximumFractionDigits(2);
                    try {
                        String promilleString = f.format(calculate.getNormalResultValue());
                        textView.setText(promilleString + " \u2030");
                        textView.setTextSize(50);
                    } catch (Exception e) {
                        textView.setText("Drücke den Button um zu starten!");
                        textView.setTextSize(20);
                    }
                });
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

}
