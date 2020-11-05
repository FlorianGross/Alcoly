package com.example.drinkly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CameraAndKI extends AppCompatActivity {
    private ArrayList<Getränke> drinks = new ArrayList<Getränke>();
    ImageView imgView;
    FirebaseAutoMLLocalModel localModel;
    FirebaseVisionImageLabeler labeler;
    FirebaseVisionImage image;
    TextView textView;
    Button openCamera, redo;
    float confidenceLevel = 0;

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
                //Restart Take Picture
                redo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CropImage.activity().start(CameraAndKI.this);
                    }
                });

                openCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sharedPreferences = getSharedPreferences("drinks", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        ArrayList<Getränke> drinks1 = new ArrayList<Getränke>();
                        Gson gson = new Gson();
                        String json = sharedPreferences.getString("List", null);
                        Type type = new TypeToken<ArrayList<Getränke>>() {
                        }.getType();
                        drinks1 = gson.fromJson(json, type);

                        if (drinks1 == null) {
                            Date date = new Date();
                            drinks.add(new Getränke(uri, date, 0.5f, 0.05f));
                        } else {
                            drinks = drinks1;
                            Date date = new Date();
                            drinks.add(new Getränke(uri, date, 0.5f, 0.05f));
                        }
                        System.out.println("Added Picture" + new Date());
                        json = gson.toJson(drinks);
                        editor.putString("List", json);
                        editor.apply();
                        openMainScreen();


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

    private void processImageLabeler(FirebaseVisionImageLabeler labeler, FirebaseVisionImage image) {
        labeler.processImage(image).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onComplete(@NonNull Task<List<FirebaseVisionImageLabel>> task) {
                for (FirebaseVisionImageLabel label : task.getResult()) {
                    String eachlabel = label.getText().toUpperCase();
                    float confidence = label.getConfidence();


                    if (confidence * 100 > 60) {
                        textView.append(eachlabel + " - " + "Successful " + confidence + "\n\n");
                        openCamera.setVisibility(View.VISIBLE);
                    } else {
                        textView.append(eachlabel + " - " + "Denied " + confidence + "\n\n");
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
