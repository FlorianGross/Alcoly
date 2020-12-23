package com.fmgross.alcoly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.gridlayout.widget.GridLayout;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fmgross.alcoly.backend.Backend_Calculation;
import com.fmgross.alcoly.backend.Backend_DatabaseHelper;
import com.fmgross.alcoly.backend.Backend_Getraenk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerLocalModel;
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerOptions;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class Activity_Camera extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView imgView, buttonTL, buttonTR, buttonBL, buttonBR, buttonBLWein, buttonBLBier, buttonBRWein, buttonBRBier, buttonTLWein, buttonTLBier, buttonTRWein, buttonTRBier;
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
    private GridLayout normal, bier, wein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_and_ki);
        buttonBL = findViewById(R.id.GridVolumeBL);
        buttonBR = findViewById(R.id.GridVolumeBR);
        buttonTL = findViewById(R.id.GridVolumeTL);
        buttonTR = findViewById(R.id.GridVolumeTR);

        buttonBLWein = findViewById(R.id.GridVolumeBLWein);
        buttonBRWein = findViewById(R.id.GridVolumeBRWein);
        buttonTLWein = findViewById(R.id.GridVolumeTLWein);
        buttonTRWein = findViewById(R.id.GridVolumeTRWein);

        buttonBLBier = findViewById(R.id.GridVolumeBLBier);
        buttonBRBier = findViewById(R.id.GridVolumeBRBier);
        buttonTLBier = findViewById(R.id.GridVolumeTLBier);
        buttonTRBier = findViewById(R.id.GridVolumeTRBier);

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

        CropImage.activity().start(Activity_Camera.this);
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
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri uri = useUri(result);
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
                onClickListener();

                redo.setOnClickListener(v -> CropImage.activity().start(Activity_Camera.this));

                openCamera.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void onClick(View v) {
                        Bitmap bitmap = null;
                        try {
                            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
                            bitmap = ImageDecoder.decodeBitmap(source);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.print("Error Converting Bitmap");
                        }

                        saveInDB(bitmap);
                        openMainScreen();
                    }

                    /**
                     * Stores the new Getränk in the Database
                     * @param bitmap the Bimap generated from the Cropper Camera
                     */
                    private void saveInDB(Bitmap bitmap) {
                        Backend_DatabaseHelper databaseHelper = new Backend_DatabaseHelper(getApplicationContext());
                        volume = getVolume();
                        permil = getPermil();
                        int SessionInt;
                        try {
                            SessionInt = getSessionInt();
                        } catch (ArrayIndexOutOfBoundsException e) {
                            SessionInt = 0;
                        }
                        Date returnDate = new Date();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(returnDate);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        String realDate = calendar.get(Calendar.DAY_OF_MONTH) + "" + month + "" + calendar.get(Calendar.YEAR);
                        int realDateTest = Integer.parseInt(realDate);
                        Backend_Getraenk getraenk = new Backend_Getraenk(type, bitmap, new Date(), volume, permil, realDateTest, SessionInt);
                        Toast.makeText(getApplicationContext(), getraenk.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(getraenk.toString());
                        databaseHelper.addOne(getraenk);

                    }

                    /**
                     * Returns the Session int from the newest Session
                     * @return int with the new Session
                     */
                    private int getSessionInt() {
                        Backend_Calculation calculator = new Backend_Calculation(getParent());
                        return calculator.getNewSessionInt();
                    }

                    /**
                     * gets the value of the Spinners item
                     * @return the value of the drink
                     */
                    private float getPermil() {
                        return seekBar.getProgress();
                    }

                    /**
                     * gets the Volume of the spinners item
                     * @return the volume of the drink selected
                     */
                    private float getVolume() {
                        System.out.println(type);
                        if (type.equals("BIERFLASCHE") || type.equals("BIERGLAS")) {
                            switch (selectedButtonBier) {
                                case 1:
                                    return 1;
                                case 2:
                                    return 0.5f;
                                case 3:
                                    return 0.3f;
                                case 4:
                                    return 0.2f;
                                default:
                                    return 0;
                            }
                        } else if (type.equals("WEINFLASCHE") || type.equals("WEINGLAS")) {
                            switch (selectedButtonWein) {
                                case 1:
                                    return 1;
                                case 2:
                                    return 0.5f;
                                case 3:
                                    return 0.2f;
                                case 4:
                                    return 0.1f;
                                default:
                                    return 0;
                            }
                        } else {
                            switch (selectedButton) {
                                case 1:
                                    return 1;
                                case 2:
                                    return 0.5f;
                                case 3:
                                    return 0.3f;
                                case 4:
                                    return 0.2f;
                                default:
                                    return 0;
                            }
                        }
                    }
                });
            }
        }
    }

    private void onClickListener() {
        //Normal
        buttonBL.setOnClickListener(v -> {
            selectedButton = 3;
            buttonBL.setBackgroundResource(R.drawable.orangerectangle);
            buttonBR.setBackgroundResource(R.drawable.blackrectangle);
            buttonTR.setBackgroundResource(R.drawable.blackrectangle);
            buttonTL.setBackgroundResource(R.drawable.blackrectangle);
        });
        buttonBR.setOnClickListener(v -> {
            selectedButton = 4;
            buttonBR.setBackgroundResource(R.drawable.orangerectangle);
            buttonBL.setBackgroundResource(R.drawable.blackrectangle);
            buttonTR.setBackgroundResource(R.drawable.blackrectangle);
            buttonTL.setBackgroundResource(R.drawable.blackrectangle);
        });
        buttonTL.setOnClickListener(v -> {
            selectedButton = 1;
            buttonTL.setBackgroundResource(R.drawable.orangerectangle);
            buttonBR.setBackgroundResource(R.drawable.blackrectangle);
            buttonTR.setBackgroundResource(R.drawable.blackrectangle);
            buttonBL.setBackgroundResource(R.drawable.blackrectangle);
        });
        buttonTR.setOnClickListener(v -> {
            selectedButton = 2;
            buttonTR.setBackgroundResource(R.drawable.orangerectangle);
            buttonBR.setBackgroundResource(R.drawable.blackrectangle);
            buttonBL.setBackgroundResource(R.drawable.blackrectangle);
            buttonTL.setBackgroundResource(R.drawable.blackrectangle);
        });
        //Bier
        buttonBLBier.setOnClickListener(v -> {
            selectedButtonBier = 3;
            buttonBLBier.setBackgroundResource(R.drawable.orangerectangle);
            buttonBRBier.setBackgroundResource(R.drawable.blackrectangle);
            buttonTRBier.setBackgroundResource(R.drawable.blackrectangle);
            buttonTLBier.setBackgroundResource(R.drawable.blackrectangle);
        });
        buttonBRBier.setOnClickListener(v -> {
            selectedButtonBier = 4;
            buttonBRBier.setBackgroundResource(R.drawable.orangerectangle);
            buttonBLBier.setBackgroundResource(R.drawable.blackrectangle);
            buttonTRBier.setBackgroundResource(R.drawable.blackrectangle);
            buttonTLBier.setBackgroundResource(R.drawable.blackrectangle);
        });
        buttonTLBier.setOnClickListener(v -> {
            selectedButtonBier = 1;
            buttonTLBier.setBackgroundResource(R.drawable.orangerectangle);
            buttonBRBier.setBackgroundResource(R.drawable.blackrectangle);
            buttonTRBier.setBackgroundResource(R.drawable.blackrectangle);
            buttonBLBier.setBackgroundResource(R.drawable.blackrectangle);
        });
        buttonTRBier.setOnClickListener(v -> {
            selectedButtonBier = 2;
            buttonTRBier.setBackgroundResource(R.drawable.orangerectangle);
            buttonBRBier.setBackgroundResource(R.drawable.blackrectangle);
            buttonBLBier.setBackgroundResource(R.drawable.blackrectangle);
            buttonTLBier.setBackgroundResource(R.drawable.blackrectangle);
        });
        //Wein
        buttonBLWein.setOnClickListener(v -> {
            selectedButtonWein = 3;
            buttonBLWein.setBackgroundResource(R.drawable.orangerectangle);
            buttonBRWein.setBackgroundResource(R.drawable.blackrectangle);
            buttonTRWein.setBackgroundResource(R.drawable.blackrectangle);
            buttonTLWein.setBackgroundResource(R.drawable.blackrectangle);
        });
        buttonBRWein.setOnClickListener(v -> {
            selectedButtonWein = 4;
            buttonBRWein.setBackgroundResource(R.drawable.orangerectangle);
            buttonBLWein.setBackgroundResource(R.drawable.blackrectangle);
            buttonTRWein.setBackgroundResource(R.drawable.blackrectangle);
            buttonTLWein.setBackgroundResource(R.drawable.blackrectangle);
        });
        buttonTLWein.setOnClickListener(v -> {
            selectedButtonWein = 1;
            buttonTLWein.setBackgroundResource(R.drawable.orangerectangle);
            buttonBRWein.setBackgroundResource(R.drawable.blackrectangle);
            buttonTRWein.setBackgroundResource(R.drawable.blackrectangle);
            buttonBLWein.setBackgroundResource(R.drawable.blackrectangle);
        });
        buttonTRWein.setOnClickListener(v -> {
            selectedButtonWein = 2;
            buttonTRWein.setBackgroundResource(R.drawable.orangerectangle);
            buttonBRWein.setBackgroundResource(R.drawable.blackrectangle);
            buttonBLWein.setBackgroundResource(R.drawable.blackrectangle);
            buttonTLWein.setBackgroundResource(R.drawable.blackrectangle);
        });
    }

    /**
     * Gets the Uri from the Cropper and labels it with the KI
     *
     * @param result the Result from the Cropper
     * @return uri, the finished Image in Uri format
     */
    private Uri useUri(CropImage.ActivityResult result) {
        Uri uri = result.getUri();
        imgView.setImageURI(uri);
        setLabelerFromLocalModel(uri);
        return uri;
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
            e.printStackTrace();
        }
        processImageLabeler(labeler, image);

    }

    /**
     * The prozess of Labeling
     *
     * @param labeler the created labeler
     * @param image   the image as InputImage
     */
    private void processImageLabeler(ImageLabeler labeler, InputImage image) {
        labeler.process(image).addOnCompleteListener(new OnCompleteListener<List<ImageLabel>>() {
            @Override
            public void onComplete(@NonNull Task<List<ImageLabel>> task) {
                boolean succes = false;
                String returnLabel = "Nicht erkannt";
                for (ImageLabel label : Objects.requireNonNull(task.getResult())) {
                    String eachlabel = label.getText().toUpperCase();
                    float confidence = label.getConfidence();
                    if (confidence * 100 > 60) {
                        succes = true;
                        returnLabel = eachlabel;
                        System.out.println(eachlabel + " " + confidence + " Success");
                    } else {
                        System.out.println(eachlabel + " " + confidence + " Decline");
                        onConfidenceDecline();
                    }
                }
                if (succes) {
                    onConfidenceSuccess(returnLabel);
                } else {
                    onConfidenceDecline();
                }
            }

            /**
             * If the process fails to recognise the getraenk
             */
            private void onConfidenceDecline() {
                drinkName.setSelection(0);
                erkannt.setVisibility(View.INVISIBLE);
                openCamera.setVisibility(View.VISIBLE);
                normal.setVisibility(View.VISIBLE);
                bier.setVisibility(View.GONE);
                wein.setVisibility(View.GONE);
                type = "error";
                buttonTR.setBackgroundResource(R.drawable.blackrectangle);
                buttonBR.setBackgroundResource(R.drawable.blackrectangle);
                buttonBL.setBackgroundResource(R.drawable.blackrectangle);
                buttonTL.setBackgroundResource(R.drawable.blackrectangle);
                openCamera.setText("Trotzdem");
            }

            /**
             * If the process succeded to recognise the getraenk
             * @param eachlabel
             */
            private void onConfidenceSuccess(String eachlabel) {
                openCamera.setVisibility(View.VISIBLE);
                erkannt.setVisibility(View.VISIBLE);
                seekBar.setProgress(5);
                openCamera.setText("Hinzufügen");

                if (eachlabel.equals("BIERGLAS") || eachlabel.equals("BIERFLASCHE")) {
                    drinkName.setSelection(1);
                    selectBeer("Bier");

                } else if (eachlabel.equals("WEINGLAS") || eachlabel.equals("WEINFLASCHE")) {
                    drinkName.setSelection(2);
                    selectWine("Wein");
                } else {
                    drinkName.setSelection(3);
                    selectElse("Schnaps");
                }


            }
        }).addOnFailureListener(e -> {
            Log.e("OnFail", "" + e);
            Toast.makeText(Activity_Camera.this, "Something went wrong! " + e, Toast.LENGTH_SHORT).show();
        });

    }

    private void selectElse(String type) {
        normal.setVisibility(View.VISIBLE);
        bier.setVisibility(View.GONE);
        wein.setVisibility(View.GONE);
        this.type = type;
        selectedButton = 2;
        buttonTR.setBackgroundResource(R.drawable.orangerectangle);
        buttonBR.setBackgroundResource(R.drawable.blackrectangle);
        buttonBL.setBackgroundResource(R.drawable.blackrectangle);
        buttonTL.setBackgroundResource(R.drawable.blackrectangle);
    }

    private void selectWine(String type) {
        wein.setVisibility(View.VISIBLE);
        bier.setVisibility(View.GONE);
        wein.setVisibility(View.GONE);
        this.type = type;
        selectedButtonWein = 3;
        buttonBRWein.setBackgroundResource(R.drawable.blackrectangle);
        buttonBLWein.setBackgroundResource(R.drawable.orangerectangle);
        buttonTLWein.setBackgroundResource(R.drawable.blackrectangle);
        buttonTRBier.setBackgroundResource(R.drawable.blackrectangle);
    }

    private void selectBeer(String type) {
        bier.setVisibility(View.VISIBLE);
        normal.setVisibility(View.GONE);
        wein.setVisibility(View.GONE);
        this.type = type;
        selectedButtonBier = 2;
        buttonBRBier.setBackgroundResource(R.drawable.blackrectangle);
        buttonBLBier.setBackgroundResource(R.drawable.blackrectangle);
        buttonTLBier.setBackgroundResource(R.drawable.blackrectangle);
        buttonTRBier.setBackgroundResource(R.drawable.orangerectangle);
    }

    private void selectNone(String type) {
        bier.setVisibility(View.GONE);
        normal.setVisibility(View.GONE);
        wein.setVisibility(View.GONE);
        this.type = type;
        selectedButtonBier = 0;
        buttonTR.setBackgroundResource(R.drawable.blackrectangle);
        buttonBR.setBackgroundResource(R.drawable.blackrectangle);
        buttonBL.setBackgroundResource(R.drawable.blackrectangle);
        buttonTL.setBackgroundResource(R.drawable.blackrectangle);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("Item Selected" + position);
        switch (position) {
            case 0:
                selectNone("None");
                openCamera.setVisibility(View.INVISIBLE);

            case 1:
                selectBeer("Bier");
                openCamera.setVisibility(View.VISIBLE);

            case 2:
                selectWine("Wein");
                openCamera.setVisibility(View.VISIBLE);
            case 3:
                selectElse("Schnaps");
                openCamera.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        openCamera.setVisibility(View.INVISIBLE);
    }
}
