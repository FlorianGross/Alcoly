package com.fmgross.alcoly.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fmgross.alcoly.R;
import com.fmgross.alcoly.backend.Backend_DatabaseHelper;
import com.fmgross.alcoly.backend.Backend_Getraenk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Fragment_Details extends Fragment {
    private Button edit;
    private TextView percentage, type, currentDate, volume;
    private EditText percentageEdit, typeEdit, volumeEdit;
    private ImageView imageView, backButton, deleteButton;
    private int current = 0;
    private Boolean saveMode = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generateDetails();

        deleteButton.setOnClickListener(v -> {
            deleteGetraenk();


        });
        backButton.setOnClickListener(v -> {

        });
        edit.setOnClickListener(v -> {


        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fragment_details, container, false);
        edit = root.findViewById(R.id.edit);
        imageView = root.findViewById(R.id.currentImage);
        percentage = root.findViewById(R.id.currentPercentage);
        type = root.findViewById(R.id.CurrentName);
        currentDate = root.findViewById(R.id.currentDate);
        backButton = root.findViewById(R.id.backButtonDetails);
        volume = root.findViewById(R.id.volume);
        percentageEdit = root.findViewById(R.id.currentPercentageEdit);
        typeEdit = root.findViewById(R.id.CurrentNameEdit);
        volumeEdit = root.findViewById(R.id.volumeEdit);
        deleteButton = root.findViewById(R.id.deleteButton);

        return root;
    }

    /**
     * When the button delete is pressed, deletes the  getraenk from the database
     */
    private void deleteGetraenk() {
        Backend_DatabaseHelper databaseHelper = new Backend_DatabaseHelper(getContext());
        ArrayList<Backend_Getraenk> arrayList = databaseHelper.getAllGetraenke();
        Bundle extras = getArguments();
        if (extras != null) {
            current = extras.getInt("intPosition");
        }
        Backend_Getraenk delGetraenk = arrayList.get(current);
        databaseHelper.deleteOne(delGetraenk);
    }

    /**
     * Generates the details page
     */
    private void generateDetails() {
        Backend_DatabaseHelper databaseHelper = new Backend_DatabaseHelper(getContext());
        ArrayList<Backend_Getraenk> arrayList = databaseHelper.getAllGetraenke();
        Bundle extras = getArguments();
        if (extras != null) {
            current = extras.getInt("intPosition");
        }
        percentage.setFocusable(false);
        type.setFocusable(false);
        percentage.setFocusableInTouchMode(false);
        type.setFocusableInTouchMode(false);
        if (saveMode = false) {
            edit.setText("Edit");
        }
        setAllValues(arrayList, current);
    }

    /**
     * sets all the values of the details view
     *
     * @param ai the list of all drinks
     * @param i  the position of the current item in the array
     */
    private void setAllValues(ArrayList<Backend_Getraenk> ai, int i) {
        type.setText(ai.get(i).getName());
        volume.setText(ai.get(i).getVolume() + " L");
        Date newDate = ai.get(i).getDate();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = formatter.format(newDate);
        currentDate.setText(strDate);
        percentage.setText(ai.get(i).getVolumePart() + " vol%");
        imageView.setImageBitmap(ai.get(i).getUri());
    }
}
