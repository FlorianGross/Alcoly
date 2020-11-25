package com.fmgross.alcoly.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmgross.alcoly.NewCalculator;
import com.fmgross.alcoly.R;

import java.text.DecimalFormat;


public class mittlererWert extends Fragment {

    private TextView age;
    private TextView weight;
    private TextView timeToDrive;
    private TextView amountOfAlc;
    private TextView promille;
    private ImageView genderImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mittlerer_wert, container, false);
        age = root.findViewById(R.id.ageTextStatsMedium);
        weight = root.findViewById(R.id.weightTextStatsMedium);
        genderImage = root.findViewById(R.id.imageView8Medium);
        timeToDrive = root.findViewById(R.id.timeToDriveMedium);
        amountOfAlc = root.findViewById(R.id.amountOfAlcoholMedium);
        TextView textType = root.findViewById(R.id.textTypeMedium);
        promille = root.findViewById(R.id.PromilleMedium);
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
        if (genderString.equals("Male")) {
            genderImage.setImageResource(R.mipmap.male);
        } else {
            genderImage.setImageResource(R.drawable.female);
        }
    }

    private void refreshData() {
        Runnable runnable = () -> {
            while (true) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            NewCalculator calculate = new NewCalculator();
                            double timeToDriveDouble = calculate.getNormalTimeToDrive(getContext().getApplicationContext()) / 60;
                            DecimalFormat f = new DecimalFormat();
                            f.setMaximumFractionDigits(2);
                            String timeToDriveString = f.format(timeToDriveDouble);
                            timeToDrive.setText(timeToDriveString + " h");
                            promille.setText(f.format(calculate.getNormalResultValue(getContext().getApplicationContext())) + " ‰");
                            amountOfAlc.setText("0");
                        } catch (Exception e) {
                            timeToDrive.setText("");
                            promille.setText("");
                            amountOfAlc.setText("");
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