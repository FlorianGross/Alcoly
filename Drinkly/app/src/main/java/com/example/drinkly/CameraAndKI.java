package com.example.drinkly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class CameraAndKI extends AppCompatActivity {
    ImageView imgView;
    FirebaseAutoMLLocalModel localModel;
    FirebaseVisionImageLabeler labeler;
    FirebaseVisionImage image;
    ProgressDialog progressDialog;
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
                //setLabelerFromLocalModel(uri);
                //Restart Take Picture
                redo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CropImage.activity().start(CameraAndKI.this);
                    }
                });
                // Use Picture
                openCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double percentage = confidenceLevel;
                    
                    }
                });
            }
        }

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
                    textView.append(eachlabel + " - " + ("" + confidence * 100).subSequence(0, 4) + "%" + "\n\n");
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

    public Bitmap getContactBitmapFromURI(Context context, Uri uri) {
        try {

            InputStream input = context.getContentResolver().openInputStream(uri);
            if (input == null) {
                return null;
            }
            return BitmapFactory.decodeStream(input);
        } catch (FileNotFoundException e) {

        }
        return null;

    }
}
    /*
    public  File saveBitmapIntoSDCardImage(Context context, Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(Util.getTempFileReceivedPath(context));
        myDir.mkdirs();

        String fname = "file_name" + ".jpg";
        File file = new File (myDir, fname);

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

     */