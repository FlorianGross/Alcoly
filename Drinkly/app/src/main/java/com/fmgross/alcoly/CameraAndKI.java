package com.fmgross.alcoly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class CameraAndKI extends AppCompatActivity {
    private ImageView imgView;
    private TextView drinkName, erkannt;
    private Button openCamera, redo;
    float volume;
    float permil;
    Spinner spinnerVolume, spinnerPermil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_and_k_i);

        openCamera = findViewById(R.id.bt_open);
        imgView = findViewById(R.id.image_view);
        drinkName = findViewById(R.id.DrinkName);
        redo = findViewById(R.id.redo);
        erkannt = findViewById(R.id.Erkannt);
        spinnerPermil = findViewById(R.id.permilSpinner);
        spinnerVolume = findViewById(R.id.volumeSpinner);

        generateSpinner();

        CropImage.activity().start(CameraAndKI.this);

    }

    /**
     * Generates the spinner and fills them with the String array
     */
    private void generateSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.permil, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPermil.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.volume, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVolume.setAdapter(adapter2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri uri = useUri(result);
                //Restart Take Picture
                redo.setOnClickListener(v -> CropImage.activity().start(CameraAndKI.this));
                //save Picture
                openCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Saves the Uri as Bitmap inside the System and returns the Path as an String
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.print("Error Converting Bitmap");
                        }
                        //Stores the Informations inside the Database
                        saveInDB(bitmap);
                        //Returns to the Main Screen
                        openMainScreen();
                    }

                    /**
                     * Stores the new Getränk in the Database
                     * @param bitmap the Bimap generated from the Cropper Camera
                     */
                    private void saveInDB(Bitmap bitmap) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                        Getraenke getraenk;
                        try {
                            volume = getVolume();
                            permil = getPermil();
                            Date returnDate = new Date();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(returnDate);
                            int month = calendar.get(Calendar.MONTH) + 1;
                            String realDate = calendar.get(Calendar.DAY_OF_MONTH) + "" + month + "" + calendar.get(Calendar.YEAR);
                            int realDateTest = Integer.parseInt(realDate);
                            getraenk = new Getraenke(bitmap, new Date(), volume, permil, realDateTest);
                            Toast.makeText(getApplicationContext(), getraenk.toString(), Toast.LENGTH_SHORT).show();
                            System.out.println(getraenk.toString());
                            databaseHelper.addOne(getraenk);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error creating Getränk", Toast.LENGTH_SHORT).show();
                            System.out.println("Error creating getrank");
                        }
                    }

                    /**
                     * gets the value of the Spinners item
                     * @return the value of the drink
                     */
                    private float getPermil() {
                        int permilItem = spinnerPermil.getSelectedItemPosition();
                        System.out.println(permilItem);
                        switch (permilItem) {
                            case 0:
                                return 45f;
                            case 1:
                                return 40f;
                            case 2:
                                return 35f;
                            case 3:
                                return 30f;
                            case 4:
                                return 25f;
                            case 5:
                                return 20f;
                            case 6:
                                return 15f;
                            case 7:
                                return 11f;
                            case 8:
                                return 8f;
                            case 9:
                                return 5.1f;
                            case 10:
                                return 2.5f;
                            case 11:
                                return 0f;
                            default:
                                return -1.0f;
                        }
                    }

                    /**
                     * gets the Volume of the spinners item
                     * @return the volume of the drink selected
                     */
                    private float getVolume() {
                        int volumeItem = spinnerVolume.getSelectedItemPosition();
                        switch (volumeItem) {
                            case 6:
                                return 0.05f;
                            case 5:
                                return 0.1f;
                            case 4:
                            case 3:
                                return 0.2f;
                            case 2:
                                return 0.3f;
                            case 1:
                                return 0.5f;
                            case 0:
                                return 1.00f;
                            default:
                                return -1.0f;
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
    }

    private void setLabelerFromLocalModel(Uri uri) {
        AutoMLImageLabelerLocalModel localModel =
                new AutoMLImageLabelerLocalModel.Builder()
                        .setAssetFilePath("model/manifest.json")
                        // or .setAbsoluteFilePath(absolute file path to manifest file)
                        .build();

        AutoMLImageLabelerOptions autoMLImageLabelerOptions =
                new AutoMLImageLabelerOptions.Builder(localModel)
                        .setConfidenceThreshold(0.0f)  // Evaluate your model in the Firebase console
                        // to determine an appropriate value.
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
                    } else {
                        System.out.println(confidence + " Decline");
                        onConfidenceDecline();
                        returnLabel = eachlabel;
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
                openCamera.setText("Trotzdem");
            }

            private void onConfidenceSuccess(String eachlabel) {
                drinkName.setText(eachlabel);
                openCamera.setVisibility(View.VISIBLE);
                erkannt.setVisibility(View.VISIBLE);
                spinnerPermil.setSelection(9);
                spinnerVolume.setSelection(1);
                openCamera.setText("Hinzufügen");
            }
        }).addOnFailureListener(e -> {
            Log.e("OnFail", "" + e);
            Toast.makeText(CameraAndKI.this, "Something went wrong! " + e, Toast.LENGTH_SHORT).show();
        });

    }

}
