package com.example.drinkly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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

import com.example.drinkly.NonMain.DatabaseHelper;
import com.example.drinkly.NonMain.Getränke;
import com.example.drinkly.NonMain.PrefConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CameraAndKI extends AppCompatActivity {
    private ArrayList<Getränke> drinks;
    private ImageView imgView;
    private FirebaseAutoMLLocalModel localModel;
    private FirebaseVisionImageLabeler labeler;
    private FirebaseVisionImage image;
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

                drinks = PrefConfig.readListFromPref(this);

                if (drinks == null) {
                    drinks = new ArrayList<Getränke>();
                }

                //Restart Take Picture
                redo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CropImage.activity().start(CameraAndKI.this);
                    }
                });
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
                        Getränke getränk;
                        try {
                            volume = getVolume();
                            permil = getPermil();

                            Date returnDate = new Date();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(returnDate);
                            int month = calendar.get(Calendar.MONTH) + 1;
                            String realDate = calendar.get(Calendar.DAY_OF_MONTH) + "" + month + "" + calendar.get(Calendar.YEAR);
                            int realDateTest = Integer.parseInt(realDate);
                            getränk = new Getränke(bitmap, new Date(), volume, permil, realDateTest);
                            Toast.makeText(getApplicationContext(), getränk.toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error creating Getränk", Toast.LENGTH_SHORT).show();
                            getränk = new Getränke(null, new Date(), -1f, -1f, -1);
                            System.out.println("Error creating getrank");
                        }

                        System.out.println(getränk.toString());
                        databaseHelper.addOne(getränk);
                    }

                    /**
                     * gets the value of the Spinners item
                     * @return the value of the drink
                     */
                    private float getPermil() {
                        int permilItem = spinnerPermil.getSelectedItemPosition();
                        switch (permilItem) {
                            case 0:
                                return 0f;
                            case 1:
                                return 2.5f;
                            case 2:
                                return 5.1f;
                            case 3:
                                return 8f;
                            case 4:
                                return 11f;
                            case 5:
                                return 15f;
                            case 6:
                                return 20f;
                            case 7:
                                return 25f;
                            case 8:
                                return 30f;
                            case 9:
                                return 35f;
                            case 10:
                                return 40f;
                            case 11:
                                return 45f;
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
                                return 0.2f;
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

    /**
     * Labels the Image with the Firebase KI
     *
     * @param uri the Image in uri format
     */
    private void setLabelerFromLocalModel(Uri uri) {
        localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath("model/manifest.json")
                .build();
        try {
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions options =
                    new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
                            .setConfidenceThreshold(0.0f)
                            .build();
            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);
            image = FirebaseVisionImage.fromFilePath(CameraAndKI.this, uri);
            processImageLabeler(labeler, image);
        } catch (FirebaseMLException | IOException e) {
            // ...
        }
    }

    /**
     * The process of Labeling
     *
     * @param labeler the labeler for the Image
     * @param image   the image to be labeled
     */
    private void processImageLabeler(FirebaseVisionImageLabeler
                                             labeler, FirebaseVisionImage image) {
        labeler.processImage(image).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onComplete(@NonNull Task<List<FirebaseVisionImageLabel>> task) {
                for (FirebaseVisionImageLabel label : task.getResult()) {
                    String eachlabel = label.getText().toUpperCase();
                    float confidence = label.getConfidence();
                    if (confidence * 100 > 60) {
                        System.out.println(confidence + " Success");
                        onConfidenceSuccess(eachlabel);
                    } else {
                        System.out.println(confidence + " Decline");
                        onConfidenceDecline();
                    }
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
                spinnerPermil.setSelection(2);
                spinnerVolume.setSelection(1);
                openCamera.setText("Hinzufügen");
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("OnFail", "" + e);
                Toast.makeText(CameraAndKI.this, "Something went wrong! " + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
