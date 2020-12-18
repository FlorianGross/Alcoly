package com.fmgross.alcoly;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmgross.alcoly.backend.DatabaseHelper;
import com.fmgross.alcoly.backend.Getraenke;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Details extends AppCompatActivity {
    private Button edit;
    private TextView percentage, type, currentDate, volume;
    private EditText percentageEdit, typeEdit, volumeEdit;
    private ImageView imageView, center, left, right, backButton, deleteButton;
    private int current = 0;
    private Boolean saveMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        center = findViewById(R.id.CenterButtonDetails);
        left = findViewById(R.id.LeftButtonDetails);
        right = findViewById(R.id.RightButtonDetails);
        edit = findViewById(R.id.edit);
        imageView = findViewById(R.id.currentImage);
        percentage = findViewById(R.id.currentPercentage);
        type = findViewById(R.id.CurrentName);
        currentDate = findViewById(R.id.currentDate);
        backButton = findViewById(R.id.backButtonDetails);
        volume = findViewById(R.id.volume);
        percentageEdit = findViewById(R.id.currentPercentageEdit);
        typeEdit = findViewById(R.id.CurrentNameEdit);
        volumeEdit = findViewById(R.id.volumeEdit);
        deleteButton = findViewById(R.id.deleteButton);


        generateDetails();

        deleteButton.setOnClickListener(v -> {
            deleteGetraenk();
            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);
        });
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewCalculator.class);
            startActivity(intent);
        });
        left.setOnClickListener(v -> {
            Intent intent = new Intent(this, Statistics.class);
            startActivity(intent);
        });
        right.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewCalculator.class);
            startActivity(intent);
        });
        center.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);
        });
        edit.setOnClickListener(v -> {


        });


    }

    /**
     * When the button delete is pressed, deletes the  getraenk from the database
     */
    private void deleteGetraenk() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        ArrayList<Getraenke> arrayList = databaseHelper.getAllGetraenke();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            current = extras.getInt("intPosition");
        }
        Getraenke delGetraenk = arrayList.get(current);
        databaseHelper.deleteOne(delGetraenk);
    }

    /**
     * Generates the details page
     */
    private void generateDetails() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        ArrayList<Getraenke> arrayList = databaseHelper.getAllGetraenke();
        Bundle extras = getIntent().getExtras();
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
    private void setAllValues(ArrayList<Getraenke> ai, int i) {
        type.setText(ai.get(i).getName());
        volume.setText(ai.get(i).getVolume() + "L");
        Date newDate = ai.get(i).getDate();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = formatter.format(newDate);
        currentDate.setText(strDate);
        percentage.setText(ai.get(i).getVolumePart() + "vol%");
        imageView.setImageBitmap(ai.get(i).getUri());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}