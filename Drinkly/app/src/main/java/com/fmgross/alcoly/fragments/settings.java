package com.fmgross.alcoly.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.fmgross.alcoly.R;

public class settings extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        SharedPreferences defaultSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = defaultSettings.edit();


        return root;

    }
}
