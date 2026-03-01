package com.fmgross.alcoly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fmgross.alcoly.backend.Backend_Calculation;
import com.fmgross.alcoly.backend.Backend_DatabaseHelper;
import com.fmgross.alcoly.backend.Backend_Getraenk;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerLocalModel;
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerOptions;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class Activity_Camera extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "Activity_Camera";
    private ImageView imgView;
    private SeekBar seekBar;
    private TextView erkannt, progressText;
    private Button openCamera, redo;
    private Spinner drinkName;
    private float volume;
    private float permil;
    private String type;
    private int selectedButton = 2;
    private int selectedButtonBier = 2;
    private int selectedButtonWein = 2;
    private ConstraintLayout normal, bier, wein, buttonTL, buttonTR, buttonBL, buttonBR, buttonBLWein, buttonBLBier, buttonBRWein, buttonBRBier, buttonTLWein, buttonTLBier, buttonTRWein, buttonTRBier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_and_ki);
        buttonBL = findViewById(R.id.ButtonNormal3);
        buttonBR = findViewById(R.id.ButtonNormal4);
        buttonTL = findViewById(R.id.ButtonNormal1);
        buttonTR = findViewById(R.id.ButtonNormal2);

        buttonBLWein = findViewById(R.id.ButtonWein3);
        buttonBRWein = findViewById(R.id.ButtonWein4);
        buttonTLWein = findViewById(R.id.ButtonWein1);
        buttonTRWein = findViewById(R.id.ButtonWein2);

        buttonBLBier = findViewById(R.id.ButtonBier3);
        buttonBRBier = findViewById(R.id.ButtonBier4);
        buttonTLBier = findViewById(R.id.ButtonBier1);
        buttonTRBier = findViewById(R.id.ButtonBier2);

        normal = findViewById(R.id.defaultGrid);
        bier = findViewById(R.id.BierGrid);
        wein = findViewById(R.id.WeinGrid);

        seekBar = findViewById(R.id.seekBar);
        openCamera = findViewById(R.id.bt_open);
        imgView = findViewById(R.id.image_view);
        drinkName = findViewById(R.id.DrinkName);
        redo = findViewById(R.id.redo);
        erkannt = findViewById(R.id.Erkannt);
        progressText = findViewById(R.id.volPerText);

        generateSpinner();
        setupButtonListeners();

        Log.d(TAG, "Starting crop activity");
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }

    private void selectButton(ConstraintLayout selected, ConstraintLayout[] others, Runnable onSelect) {
        selected.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
        for (ConstraintLayout other : others) {
            other.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
        }
        onSelect.run();
    }

    private void setupButtonListeners() {
        // Normal
        buttonBL.setOnClickListener(v -> selectButton(buttonBL, new ConstraintLayout[]{buttonBR, buttonTR, buttonTL}, () -> selectedButton = 3));
        buttonBR.setOnClickListener(v -> selectButton(buttonBR, new ConstraintLayout[]{buttonBL, buttonTR, buttonTL}, () -> selectedButton = 4));
        buttonTL.setOnClickListener(v -> selectButton(buttonTL, new ConstraintLayout[]{buttonBR, buttonTR, buttonBL}, () -> selectedButton = 1));
        buttonTR.setOnClickListener(v -> selectButton(buttonTR, new ConstraintLayout[]{buttonBR, buttonBL, buttonTL}, () -> selectedButton = 2));
        // Bier
        buttonBLBier.setOnClickListener(v -> selectButton(buttonBLBier, new ConstraintLayout[]{buttonBRBier, buttonTRBier, buttonTLBier}, () -> selectedButtonBier = 3));
        buttonBRBier.setOnClickListener(v -> selectButton(buttonBRBier, new ConstraintLayout[]{buttonBLBier, buttonTRBier, buttonTLBier}, () -> selectedButtonBier = 4));
        buttonTLBier.setOnClickListener(v -> selectButton(buttonTLBier, new ConstraintLayout[]{buttonBRBier, buttonTRBier, buttonBLBier}, () -> selectedButtonBier = 1));
        buttonTRBier.setOnClickListener(v -> selectButton(buttonTRBier, new ConstraintLayout[]{buttonBRBier, buttonBLBier, buttonTLBier}, () -> selectedButtonBier = 2));
        // Wein
        buttonBLWein.setOnClickListener(v -> selectButton(buttonBLWein, new ConstraintLayout[]{buttonBRWein, buttonTRWein, buttonTLWein}, () -> selectedButtonWein = 3));
        buttonBRWein.setOnClickListener(v -> selectButton(buttonBRWein, new ConstraintLayout[]{buttonBLWein, buttonTRWein, buttonTLWein}, () -> selectedButtonWein = 4));
        buttonTLWein.setOnClickListener(v -> selectButton(buttonTLWein, new ConstraintLayout[]{buttonBRWein, buttonTRWein, buttonBLWein}, () -> selectedButtonWein = 1));
        buttonTRWein.setOnClickListener(v -> selectButton(buttonTRWein, new ConstraintLayout[]{buttonBRWein, buttonBLWein, buttonTLWein}, () -> selectedButtonWein = 2));
    }

    /**
     * Generates the spinner with the ArrayList
     */
    private void generateSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drinkName.setAdapter(adapter);
        drinkName.setOnItemSelectedListener(this);
    }

    /**
     * the result from the cropper Activity
     *
     * @param requestCode the requestcode to validate the data are for the right location
     * @param resultCode  the resultCode to validate the data from the result
     * @param data        the data for the Image
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    imgView.setImageURI(data.getData());
                    setLabelerFromLocalModel(data.getData());
                    FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", new File(data.getData().getPath()));
                }
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progressText.setText(progress + " vol%");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                redo.setOnClickListener(v -> {
                    Intent intent = new Intent(this, getClass());
                    startActivity(intent);
                });

                openCamera.setOnClickListener(v -> {
                    Bitmap bitmap = null;
                    try {
                        if (data != null) {
                            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), data.getData());
                            bitmap = ImageDecoder.decodeBitmap(source);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error converting bitmap", e);
                    }

                    saveInDB(bitmap);
                    openMainScreen();
                });
            }
        }
    }

    /**
     * Stores the new Getraenk in the Database
     *
     * @param bitmap the Bitmap generated from the Cropper Camera
     */
    private void saveInDB(Bitmap bitmap) {
        Backend_DatabaseHelper databaseHelper = new Backend_DatabaseHelper(getApplicationContext());
        volume = getVolume();
        permil = seekBar.getProgress();
        int sessionInt;
        try {
            Backend_Calculation calculator = new Backend_Calculation(getApplicationContext());
            sessionInt = calculator.getNewSessionInt();
        } catch (Exception e) {
            sessionInt = 0;
        }
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH) * 1000000;
        int month = (calendar.get(Calendar.MONTH) + 1) * 10000;
        int year = calendar.get(Calendar.YEAR);
        int realDate = day + month + year;
        Backend_Getraenk getraenk = new Backend_Getraenk(type, bitmap, new Date(), volume, permil, realDate, sessionInt);
        databaseHelper.addOne(getraenk);
    }

    /**
     * Gets the Volume based on drink type and selected button
     *
     * @return the volume of the drink selected
     */
    private float getVolume() {
        if ("Bier".equals(type)) {
            switch (selectedButtonBier) {
                case 1: return 1;
                case 2: return 0.5f;
                case 3: return 0.3f;
                case 4: return 0.2f;
                default: return 0;
            }
        } else if ("Wein".equals(type)) {
            switch (selectedButtonWein) {
                case 1: return 1;
                case 2: return 0.5f;
                case 3: return 0.2f;
                case 4: return 0.1f;
                default: return 0;
            }
        } else {
            switch (selectedButton) {
                case 1: return 1;
                case 2: return 0.5f;
                case 3: return 0.3f;
                case 4: return 0.2f;
                default: return 0;
            }
        }
    }

    /**
     * Opens the MainScreen window
     */
    private void openMainScreen() {
        Intent intent = new Intent(this, Activity_MainPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * creates the labeler for the Uri image
     *
     * @param uri the Uri of the Image
     */
    private void setLabelerFromLocalModel(Uri uri) {
        AutoMLImageLabelerLocalModel localModel =
                new AutoMLImageLabelerLocalModel.Builder()
                        .setAssetFilePath("model/manifest.json")
                        .build();

        AutoMLImageLabelerOptions autoMLImageLabelerOptions =
                new AutoMLImageLabelerOptions.Builder(localModel)
                        .setConfidenceThreshold(0.0f)
                        .build();
        ImageLabeler labeler = ImageLabeling.getClient(autoMLImageLabelerOptions);
        InputImage image = null;
        try {
            image = InputImage.fromFilePath(this, uri);
        } catch (IOException e) {
            Log.e(TAG, "Error with labeling", e);
        }
        processImageLabeler(labeler, image);
    }

    /**
     * The process of Labeling
     *
     * @param labeler the created labeler
     * @param image   the image as InputImage
     */
    private void processImageLabeler(ImageLabeler labeler, InputImage image) {
        labeler.process(image).addOnCompleteListener((@NonNull Task<List<ImageLabel>> task) -> {
            boolean success = false;
            String returnLabel = "Nicht erkannt";
            for (ImageLabel label : Objects.requireNonNull(task.getResult())) {
                String eachLabel = label.getText().toUpperCase();
                float confidence = label.getConfidence();
                if (confidence > 0.6f) {
                    success = true;
                    returnLabel = eachLabel;
                    Log.d(TAG, eachLabel + " " + confidence + " Success");
                } else {
                    Log.d(TAG, eachLabel + " " + confidence + " Decline");
                }
            }
            if (success) {
                onConfidenceSuccess(returnLabel);
            } else {
                onConfidenceDecline();
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Image labeling failed", e);
            Toast.makeText(Activity_Camera.this, "Something went wrong! " + e, Toast.LENGTH_SHORT).show();
        });
    }

    private void onConfidenceDecline() {
        drinkName.setSelection(0);
        erkannt.setVisibility(View.INVISIBLE);
        openCamera.setVisibility(View.GONE);
        normal.setVisibility(View.VISIBLE);
        bier.setVisibility(View.GONE);
        wein.setVisibility(View.GONE);
        type = "error";
        buttonTR.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
        buttonBR.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
        buttonBL.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
        buttonTL.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
        seekBar.setProgress(0);
    }

    private void onConfidenceSuccess(String eachLabel) {
        openCamera.setVisibility(View.VISIBLE);
        erkannt.setVisibility(View.VISIBLE);

        if (eachLabel.equals("BIERGLAS") || eachLabel.equals("BIERFLASCHE")) {
            drinkName.setSelection(1);
            seekBar.setProgress(5);
            selectBeer();
        } else if (eachLabel.equals("WEINGLAS") || eachLabel.equals("WEINFLASCHE")) {
            drinkName.setSelection(2);
            seekBar.setProgress(11);
            selectWine();
        } else {
            drinkName.setSelection(3);
            seekBar.setProgress(5);
            selectElse();
        }
    }

    private void selectElse() {
        normal.setVisibility(View.VISIBLE);
        bier.setVisibility(View.GONE);
        wein.setVisibility(View.GONE);
        this.type = "Custom";
        selectedButton = 2;
        buttonTR.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
        buttonBR.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
        buttonBL.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
        buttonTL.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
    }

    private void selectWine() {
        wein.setVisibility(View.VISIBLE);
        bier.setVisibility(View.GONE);
        normal.setVisibility(View.GONE);
        this.type = "Wein";
        selectedButtonWein = 3;
        buttonBRWein.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
        buttonBLWein.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
        buttonTLWein.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
        buttonTRWein.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
    }

    private void selectBeer() {
        bier.setVisibility(View.VISIBLE);
        normal.setVisibility(View.GONE);
        wein.setVisibility(View.GONE);
        this.type = "Bier";
        selectedButtonBier = 3;
        buttonBRBier.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
        buttonBLBier.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
        buttonTLBier.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
        buttonTRBier.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
    }

    private void selectNone() {
        bier.setVisibility(View.GONE);
        normal.setVisibility(View.GONE);
        wein.setVisibility(View.GONE);
        this.type = "None";
        selectedButtonBier = 0;
        buttonTR.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
        buttonBR.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
        buttonBL.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
        buttonTL.setBackground(VectorDrawableCompat.create(getResources(), R.drawable.outlinefile, null));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                selectNone();
                openCamera.setVisibility(View.GONE);
                break;
            case 1:
                selectBeer();
                openCamera.setVisibility(View.VISIBLE);
                break;
            case 2:
                selectWine();
                openCamera.setVisibility(View.VISIBLE);
                break;
            case 3:
                selectElse();
                openCamera.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        openCamera.setVisibility(View.GONE);
    }
}
