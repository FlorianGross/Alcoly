package com.fmgross.alcoly.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fmgross.alcoly.Activity_Camera;
import com.fmgross.alcoly.R;
import com.fmgross.alcoly.backend.Backend_Calculation;

import java.text.DecimalFormat;

public class Fragment_Main extends Fragment {

    private TextView textView;
    private final boolean running = true;
    private ImageView mainButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        mainButton = root.findViewById(R.id.mainAction);
        textView = root.findViewById(R.id.promilleErgebnis);
        mainButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), Activity_Camera.class);
            startActivity(intent);
        });
        refreshData();
        return root;
    }

    /**
     * Refreshes the values from the main page
     */
    private void refreshData() {
        Runnable runnable = () -> {
            while (running) {
                getActivity().runOnUiThread(() -> {
                    Backend_Calculation calculate = new Backend_Calculation(getContext());
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
