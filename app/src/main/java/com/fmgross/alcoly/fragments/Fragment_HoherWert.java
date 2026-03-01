package com.fmgross.alcoly.fragments;


import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fmgross.alcoly.R;
import com.fmgross.alcoly.backend.Backend_Calculation;

import java.text.DecimalFormat;


public class Fragment_HoherWert extends Fragment {

    private static final long REFRESH_INTERVAL_MS = 10000;
    private TextView timeToDrive, amountOfAlc, promille, textType;
    private final Handler refreshHandler = new Handler(Looper.getMainLooper());
    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            setData();
            refreshHandler.postDelayed(this, REFRESH_INTERVAL_MS);
        }
    };

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
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshHandler.post(refreshRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        refreshHandler.removeCallbacks(refreshRunnable);
    }

    private void setData() {
        if (getContext() == null || getActivity() == null) return;
        try {
            Backend_Calculation calculate = new Backend_Calculation(getContext());
            int hours = (int) (calculate.getHighTimeToDrive() / 60);
            int minutes = (int) (calculate.getHighTimeToDrive() % 60);
            String time = hours + ":" + String.format("%02d", minutes);
            DecimalFormat f = new DecimalFormat();
            f.setMaximumFractionDigits(2);
            int getraenke = calculate.getSessionAmount();
            textType.setText(getraenke + " alkoholische Getränke");
            timeToDrive.setText(time + " h");
            promille.setText(f.format(calculate.getHighResultValue()) + " \u2030");
            amountOfAlc.setText(f.format(calculate.getAmountOfAlcResult()) + " g");
        } catch (Exception e) {
            timeToDrive.setText("0:00 h");
            promille.setText("0.0 \u2030");
            amountOfAlc.setText("0 ml");
            textType.setText("0 alkoholische Getränke");
        }
    }
}
