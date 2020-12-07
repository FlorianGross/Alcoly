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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fmgross.alcoly.backend.DatabaseHelper;
import com.fmgross.alcoly.backend.Getraenke;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class CameraAndKI extends AppCompatActivity {
    private ImageView imgView, buttonTL, buttonTR, buttonBL, buttonBR;
    private SeekBar seekBar;
    private TextView drinkName, erkannt, progressText;
    private Button openCamera, redo;
    private float volume;
    private float permil;
    private String type;
    private int selectedButton = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_and_ki);
        buttonBL = findViewById(R.id.GridVolumeBL);
        buttonBR = findViewById(R.id.GridVolumeBR);
        buttonTL = findViewById(R.id.GridVolumeTL);
        buttonTR = findViewById(R.id.GridVolumeTR);
        seekBar = findViewById(R.id.seekBar);
        openCamera = findViewById(R.id.bt_open);
        imgView = findViewById(R.id.image_view);
        drinkName = findViewById(R.id.DrinkName);
        redo = findViewById(R.id.redo);
        erkannt = findViewById(R.id.Erkannt);
        progressText = findViewById(R.id.volPerText);


        CropImage.activity().start(CameraAndKI.this);
    }


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

                redo.setOnClickListener(v -> CropImage.activity().start(CameraAndKI.this));

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
                        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                        volume = getVolume();
                        permil = getPermil();
                        int SessionInt;
                        try {
                            SessionInt = getSessionInt();
                        } catch (Exception e) {
                            SessionInt = 0;
                        }
                        Date returnDate = new Date();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(returnDate);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        String realDate = calendar.get(Calendar.DAY_OF_MONTH) + "" + month + "" + calendar.get(Calendar.YEAR);
                        int realDateTest = Integer.parseInt(realDate);
                        Getraenke getraenk = new Getraenke("Bier" + type, bitmap, new Date(), volume, permil, realDateTest, SessionInt);
                        Toast.makeText(getApplicationContext(), getraenk.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(getraenk.toString());
                        databaseHelper.addOne(getraenk);

                    }

                    private int getSessionInt() {
                        NewCalculator calculator = new NewCalculator();
                        double promille = calculator.getMinResultValue(getParent());
                        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                        ArrayList<Getraenke> arrayList = databaseHelper.getAllGetraenke();
                        if (promille <= 0) {
                            return arrayList.get(0).getSession() + 1;
                        } else {
                            return arrayList.get(0).getSession();
                        }
                    }

                    /**
                     * gets the value of the Spinners item
                     * @return the value of the drink
                     */
                    private float getPermil() {
                        return seekBar.getX();
                    }

                    /**
                     * gets the Volume of the spinners item
                     * @return the volume of the drink selected
                     */
                    private float getVolume() {
                        if (type.equals("Glas") || type.equals("Flasche")) {
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
                        } else if (type.equals("Wine")) {
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
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

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
                    } else {
                        System.out.println(confidence + " Decline");
                        onConfidenceDecline();
                    }
                }
                if (succes) {
                    onConfidenceSuccess(returnLabel);
                } else {
                    onConfidenceDecline();
                }
            }

            private void onConfidenceDecline() {
                drinkName.setText("Nicht Erkannt");
                erkannt.setVisibility(View.INVISIBLE);
                openCamera.setVisibility(View.VISIBLE);
                buttonTR.setBackgroundResource(R.drawable.blackrectangle);
                buttonBR.setBackgroundResource(R.drawable.blackrectangle);
                buttonBL.setBackgroundResource(R.drawable.blackrectangle);
                buttonTL.setBackgroundResource(R.drawable.blackrectangle);
                openCamera.setText("Trotzdem");
            }

            private void onConfidenceSuccess(String eachlabel) {
                drinkName.setText(eachlabel);
                type = eachlabel;
                openCamera.setVisibility(View.VISIBLE);
                erkannt.setVisibility(View.VISIBLE);
                seekBar.setX(5f);
                selectedButton = 2;
                buttonTR.setBackgroundResource(R.drawable.orangerectangle);
                buttonBR.setBackgroundResource(R.drawable.blackrectangle);
                buttonBL.setBackgroundResource(R.drawable.blackrectangle);
                buttonTL.setBackgroundResource(R.drawable.blackrectangle);
                openCamera.setText("Hinzufügen");

                if (eachlabel.equals("Glas") || eachlabel.equals("Flasche")) {
                   /*  buttonTR.setForeground();
                    buttonTL.setForeground();
                    buttonBL.setForeground();
                    buttonBR.setForeground(); */
                } else if (eachlabel.equals("Wein")) {
                    /* buttonTR.setForeground();
                    buttonTL.setForeground();
                    buttonBL.setForeground();
                    buttonBR.setForeground(); */
                } else {
                    /* buttonTR.setForeground();
                    buttonTL.setForeground();
                    buttonBL.setForeground();
                    buttonBR.setForeground(); */
                }


            }
        }).addOnFailureListener(e -> {
            Log.e("OnFail", "" + e);
            Toast.makeText(CameraAndKI.this, "Something went wrong! " + e, Toast.LENGTH_SHORT).show();
        });

    }

}
