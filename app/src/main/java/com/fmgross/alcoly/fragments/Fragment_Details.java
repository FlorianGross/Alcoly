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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
    private ImageView imageView;
    private boolean saveMode = false;
    private Backend_Getraenk getraenk;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_details, container, false);
        edit = root.findViewById(R.id.edit);
        imageView = root.findViewById(R.id.currentImage);
        percentage = root.findViewById(R.id.currentPercentage);
        type = root.findViewById(R.id.CurrentName);
        currentDate = root.findViewById(R.id.currentDate);
        ImageView backButton = root.findViewById(R.id.backButtonDetails);
        volume = root.findViewById(R.id.volume);
        percentageEdit = root.findViewById(R.id.currentPercentageEdit);
        typeEdit = root.findViewById(R.id.CurrentNameEdit);
        volumeEdit = root.findViewById(R.id.volumeEdit);
        ImageView deleteButton = root.findViewById(R.id.deleteButton);

        generateDetails();

        deleteButton.setOnClickListener(v -> {
            deleteGetraenk();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.nav_host_fragment, new Fragment_Timeline());
            ft.commit();

        });
        backButton.setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.nav_host_fragment, new Fragment_Timeline());
            ft.commit();
        });
        edit.setOnClickListener(v -> {
            if (!saveMode) {
                percentageEdit.setText(percentage.getText());
                volumeEdit.setText(volume.getText());
                typeEdit.setText(type.getText());
                percentage.setVisibility(View.GONE);
                volume.setVisibility(View.GONE);
                type.setVisibility(View.GONE);
                percentageEdit.setVisibility(View.VISIBLE);
                typeEdit.setVisibility(View.VISIBLE);
                volumeEdit.setVisibility(View.VISIBLE);
                saveMode = true;
                edit.setText("Speichern");
            } else {
                try {
                    percentage.setText(percentageEdit.getText());
                    volume.setText(volumeEdit.getText());
                    type.setText(typeEdit.getText());
                    percentage.setVisibility(View.VISIBLE);
                    volume.setVisibility(View.VISIBLE);
                    type.setVisibility(View.VISIBLE);
                    percentageEdit.setVisibility(View.GONE);
                    typeEdit.setVisibility(View.GONE);
                    volumeEdit.setVisibility(View.GONE);
                    saveMode = false;
                    edit.setText("Ändern");
                    Backend_Getraenk test = getraenk;
                    Backend_DatabaseHelper dbHelper = new Backend_DatabaseHelper(getContext());
                    dbHelper.deleteOne(getraenk);
                    test.setName(typeEdit.getText().toString());
                    test.setVolume(Float.parseFloat(volumeEdit.getText().toString()));
                    test.setVolumePart(Float.parseFloat(percentageEdit.getText().toString()));
                    dbHelper.addOne(test);
                } catch (Exception e) {
                    Toast.makeText(getContext().getApplicationContext(), "Fehler bei Ihrer Eingabe", Toast.LENGTH_SHORT).show();
                }
            }

        });
        return root;
    }

    private void generateDetails() {
        Bundle extras = getArguments();
        if (extras != null) {
            int realDate = extras.getInt("intRealDate");
            int current = extras.getInt("intposition");
            System.out.println("intRealDate: " + realDate + "IntPosition:" + current);
            Backend_DatabaseHelper databaseHelper = new Backend_DatabaseHelper(getContext());
            ArrayList<Backend_Getraenk> db = databaseHelper.getAllOfDate(realDate);
            System.out.println(db.toString());
            getraenk = db.get(current);
            System.out.println(getraenk.toString());

        } else {
            throw new IllegalArgumentException("No Getraenk selected");
        }
        setAllValues(getraenk);
    }

    /**
     * When the button delete is pressed, deletes the  getraenk from the database
     */
    private void deleteGetraenk() {
        Backend_DatabaseHelper databaseHelper = new Backend_DatabaseHelper(getContext());
        databaseHelper.deleteOne(getraenk);
    }


    private void setAllValues(Backend_Getraenk getraenk) {
        type.setText(getraenk.getName());
        volume.setText(getraenk.getVolume() + "");
        Date newDate = getraenk.getDate();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = formatter.format(newDate);
        currentDate.setText(strDate);
        percentage.setText(getraenk.getVolumePart() + "");
        imageView.setImageBitmap(getraenk.getUri());
    }
}
