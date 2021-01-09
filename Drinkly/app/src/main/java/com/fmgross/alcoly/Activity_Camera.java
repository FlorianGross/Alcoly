package com.fmgross.alcoly;

import androidx.annotation.DrawableRes;
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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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
        System.out.println("start crop activity");
        CropImage.activity().start(this);
        //Normal
        buttonBL.setOnClickListener(v -> {
            System.out.println("ButtonBl selected");
            selectedButton = 3;
            buttonBL.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonBR.setOnClickListener(v -> {
            System.out.println("ButtonBR selected");
            selectedButton = 4;
            buttonBR.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonTL.setOnClickListener(v -> {
            System.out.println("ButtonTL selected");
            selectedButton = 1;
            buttonTL.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonBL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonTR.setOnClickListener(v -> {
            System.out.println("ButtonTR selected");
            selectedButton = 2;
            buttonTR.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonBL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        //Bier
        buttonBLBier.setOnClickListener(v -> {
            System.out.println("ButtonBLBier selected");
            selectedButtonBier = 3;
            buttonBLBier.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTLBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonBRBier.setOnClickListener(v -> {
            System.out.println("ButtonBRBier selected");
            selectedButtonBier = 4;
            buttonBRBier.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBLBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTLBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonTLBier.setOnClickListener(v -> {
            System.out.println("ButtonTlBier Selected");
            selectedButtonBier = 1;
            buttonTLBier.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonBLBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonTRBier.setOnClickListener(v -> {
            System.out.println("ButtonTRBier Selected");
            selectedButtonBier = 2;
            buttonTRBier.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonBLBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTLBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        //Wein
        buttonBLWein.setOnClickListener(v -> {
            System.out.println("ButtonBLWein Selected");
            selectedButtonWein = 3;
            buttonBLWein.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBRWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTRWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTLWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonBRWein.setOnClickListener(v -> {
            System.out.println("ButtonBRWein Selected");
            selectedButtonWein = 4;
            buttonBRWein.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBLWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTRWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTLWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonTLWein.setOnClickListener(v -> {
            System.out.println("ButtonTLWein Selected");
            selectedButtonWein = 1;
            buttonTLWein.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBRWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTRWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonBLWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonTRWein.setOnClickListener(v -> {
            System.out.println("ButtonTRWein Selected");
            selectedButtonWein = 2;
            buttonTRWein.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBRWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonBLWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTLWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
    }

    /**
     * Generates the spinner with the ArrayList
     */
    private void generateSpinner() {
        System.out.println("Generate Spinner");
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
                     *
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
                     *
                     * @return int with the new Session
                     */
                    private int getSessionInt() {
                        Backend_Calculation calculator = new Backend_Calculation(getApplicationContext());
                        return calculator.getNewSessionInt();
                    }

                    /**
                     * gets the value of the Spinners item
                     *
                     * @return the value of the drink
                     */
                    private float getPermil() {
                        return seekBar.getProgress();
                    }

                    /**
                     * gets the Volume of the spinners item
                     *
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
        System.out.println("Open Main Screen");
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
        System.out.println("setLabelerFromLocalModel");
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
            System.out.println("Error with labeling");
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
        System.out.println("Process Image Labeler");
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
                openCamera.setVisibility(View.GONE);
                normal.setVisibility(View.VISIBLE);
                bier.setVisibility(View.GONE);
                wein.setVisibility(View.GONE);
                type = "error";
                buttonTR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
                buttonBR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
                buttonBL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
                buttonTL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
                seekBar.setProgress(0);
            }

            /**
             * If the process succeded to recognise the getraenk
             * @param eachlabel
             */
            private void onConfidenceSuccess(String eachlabel) {
                openCamera.setVisibility(View.VISIBLE);
                erkannt.setVisibility(View.VISIBLE);

                if (eachlabel.equals("BIERGLAS") || eachlabel.equals("BIERFLASCHE")) {
                    drinkName.setSelection(1);
                    seekBar.setProgress(5);
                    selectBeer("Bier");

                } else if (eachlabel.equals("WEINGLAS") || eachlabel.equals("WEINFLASCHE")) {
                    drinkName.setSelection(2);
                    seekBar.setProgress(11);
                    selectWine("Wein");
                } else {
                    drinkName.setSelection(3);
                    seekBar.setProgress(5);
                    selectElse("Custom");
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
        buttonTR.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
        buttonBR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        buttonBL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        buttonTL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
    }

    private void selectWine(String type) {
        wein.setVisibility(View.VISIBLE);
        bier.setVisibility(View.GONE);
        normal.setVisibility(View.GONE);
        this.type = type;
        selectedButtonWein = 3;
        buttonBRWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        buttonBLWein.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
        buttonTLWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        buttonTRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
    }

    private void selectBeer(String type) {
        bier.setVisibility(View.VISIBLE);
        normal.setVisibility(View.GONE);
        wein.setVisibility(View.GONE);
        this.type = type;
        selectedButtonBier = 3;
        buttonBRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        buttonBLBier.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
        buttonTLBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        buttonTRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
    }

    private void selectNone(String type) {
        bier.setVisibility(View.GONE);
        normal.setVisibility(View.GONE);
        wein.setVisibility(View.GONE);
        this.type = type;
        selectedButtonBier = 0;
        buttonTR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        buttonBR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        buttonBL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        buttonTL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("Item Selected" + position);
        switch (position) {
            case 0:
                selectNone("None");
                openCamera.setVisibility(View.GONE);
                break;

            case 1:
                selectBeer("Bier");
                openCamera.setVisibility(View.VISIBLE);
                break;

            case 2:
                selectWine("Wein");
                openCamera.setVisibility(View.VISIBLE);
                break;
            case 3:
                selectElse("Schnaps");
                openCamera.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        openCamera.setVisibility(View.GONE);
    }

    private void onClickListener() {
        //Normal
        buttonBL.setOnClickListener(v -> {
            System.out.println("ButtonBl selected");
            selectedButton = 3;
            buttonBL.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonBR.setOnClickListener(v -> {
            System.out.println("ButtonBR selected");
            selectedButton = 4;
            buttonBR.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonTL.setOnClickListener(v -> {
            System.out.println("ButtonTL selected");
            selectedButton = 1;
            buttonTL.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonBL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonTR.setOnClickListener(v -> {
            System.out.println("ButtonTR selected");
            selectedButton = 2;
            buttonTR.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBR.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonBL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTL.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        //Bier
        buttonBLBier.setOnClickListener(v -> {
            System.out.println("ButtonBLBier selected");
            selectedButtonBier = 3;
            buttonBLBier.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTLBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonBRBier.setOnClickListener(v -> {
            System.out.println("ButtonBRBier selected");
            selectedButtonBier = 4;
            buttonBRBier.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBLBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTLBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonTLBier.setOnClickListener(v -> {
            System.out.println("ButtonTlBier Selected");
            selectedButtonBier = 1;
            buttonTLBier.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonBLBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonTRBier.setOnClickListener(v -> {
            System.out.println("ButtonTRBier Selected");
            selectedButtonBier = 2;
            buttonTRBier.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBRBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonBLBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTLBier.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        //Wein
        buttonBLWein.setOnClickListener(v -> {
            System.out.println("ButtonBLWein Selected");
            selectedButtonWein = 3;
            buttonBLWein.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBRWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTRWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTLWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonBRWein.setOnClickListener(v -> {
            System.out.println("ButtonBRWein Selected");
            selectedButtonWein = 4;
            buttonBRWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonBLWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTRWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTLWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonTLWein.setOnClickListener(v -> {
            System.out.println("ButtonTLWein Selected");
            selectedButtonWein = 1;
            buttonTLWein.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBRWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTRWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonBLWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
        buttonTRWein.setOnClickListener(v -> {
            System.out.println("ButtonTRWein Selected");
            selectedButtonWein = 2;
            buttonTRWein.setBackgroundColor(getResources().getColor(R.color.MainColor, null));
            buttonBRWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonBLWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
            buttonTLWein.setBackground(getResources().getDrawable(R.drawable.outlinefile, null));
        });
    }
}
