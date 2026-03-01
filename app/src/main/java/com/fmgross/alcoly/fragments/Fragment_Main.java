package com.fmgross.alcoly.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

    private static final long REFRESH_INTERVAL_MS = 10000;
    private TextView textView, timeToDrive;
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
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ImageView mainButton = root.findViewById(R.id.mainAction);
        textView = root.findViewById(R.id.promilleErgebnis);
        timeToDrive = root.findViewById(R.id.timeToDrive);
        mainButton.setOnClickListener(view -> {
            if (getContext() == null) return;
            SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            boolean audio = settings.getBoolean("audio", true);
            if (audio) {
                MediaPlayer sound = MediaPlayer.create(getContext(), R.raw.blop);
                sound.setOnCompletionListener(MediaPlayer::release);
                sound.start();
            }
            Intent intent = new Intent(getActivity(), Activity_Camera.class);
            startActivity(intent);
        });
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
            DecimalFormat f = new DecimalFormat();
            f.setMaximumFractionDigits(2);
            String promilleString = f.format(calculate.getNormalResultValue());
            int hours = (int) (calculate.getNormalTimeToDrive() / 60);
            int minutes = (int) (calculate.getNormalTimeToDrive() % 60);
            String time = hours + ":" + String.format("%02d", minutes);
            textView.setText(promilleString + " \u2030");
            timeToDrive.setText(time + " h");
        } catch (Exception e) {
            textView.setText("0 \u2030");
            timeToDrive.setText("0:00 h");
        }
    }
}
