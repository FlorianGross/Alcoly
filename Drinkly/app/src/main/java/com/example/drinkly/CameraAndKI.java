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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drinkly.NonMain.DatabaseHelper;
import com.example.drinkly.NonMain.Getränke;
import com.example.drinkly.NonMain.PrefConfig;
import com.example.drinkly.oldClass.Calculator;
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
    private TextView textView;
    private Button openCamera, redo;
    private float confidenceLevel = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_and_k_i);

        openCamera = findViewById(R.id.bt_open);
        imgView = findViewById(R.id.image_view);
        textView = findViewById(R.id.textView);
        redo = findViewById(R.id.redo);
        CropImage.activity().start(CameraAndKI.this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri uri = result.getUri();
                imgView.setImageURI(uri);
                setLabelerFromLocalModel(uri);

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
                        //Checks for Permission
                        ActivityCompat.requestPermissions(CameraAndKI.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        ActivityCompat.requestPermissions(CameraAndKI.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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

                    private void saveInDB(Bitmap bitmap) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                        Getränke getränk;
                        try {
                            Date returnDate = new Date();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(returnDate);
                            int month = calendar.get(Calendar.MONTH) + 1;
                            String realDate = calendar.get(Calendar.DAY_OF_MONTH) + "" + month + "" + calendar.get(Calendar.YEAR);
                            int realDateTest = Integer.parseInt(realDate);
                            getränk = new Getränke(bitmap, new Date(), 0.5f, 5.0f, realDateTest);
                            Toast.makeText(getApplicationContext(), getränk.toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error creating getränk", Toast.LENGTH_SHORT).show();
                            getränk = new Getränke(null, new Date(), -1, -1, -1);
                            System.out.println("Error creating getrank");
                        }

                        System.out.println(getränk.toString());
                        boolean success = databaseHelper.addOne(getränk);
                        //Toast.makeText(getApplicationContext(), "Success = " + success, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void openMainScreen() {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }

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

    private void processImageLabeler(FirebaseVisionImageLabeler
                                             labeler, FirebaseVisionImage image) {
        labeler.processImage(image).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onComplete(@NonNull Task<List<FirebaseVisionImageLabel>> task) {
                for (FirebaseVisionImageLabel label : task.getResult()) {
                    String eachlabel = label.getText().toUpperCase();
                    float confidence = label.getConfidence();


                    if (confidence * 100 > 60) {
                        textView.append(eachlabel + " - " + "Successful " + "\n\n");
                        openCamera.setVisibility(View.VISIBLE);
                    } else {
                        textView.append(eachlabel + " - " + "Denied " + "\n\n");
                    }
                    if (confidence > confidenceLevel) {
                        confidenceLevel = confidence;
                    }
                }
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
