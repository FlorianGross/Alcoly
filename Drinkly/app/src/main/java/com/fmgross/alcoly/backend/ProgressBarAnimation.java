package com.fmgross.alcoly.backend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

import com.fmgross.alcoly.CameraAndKI;
import com.fmgross.alcoly.MainPage;
import com.fmgross.alcoly.MainScreen;

public class ProgressBarAnimation extends Animation {
    private final Context context;
    private final ProgressBar progressBar;
    private final float from;
    private final float to;

    public ProgressBarAnimation(Context context, ProgressBar progressBar, float from, float to) {
        this.context = context;
        this.progressBar = progressBar;
        this.from = from;
        this.to = to;
    }

    /**
     * Applies the Transformation to the progressBar
     *
     * @param interpolatedTime the time, the Bar needs to fill
     * @param t                the percentage, the progress bar is filled
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);

        if (value == to) {
            SharedPreferences settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
            boolean scanOnStart = settings.getBoolean("scanOnStart", false);
            if (scanOnStart) {
                context.startActivity(new Intent(context, CameraAndKI.class));
            } else {
                context.startActivity(new Intent(context, MainPage.class));
            }
        }
    }
}
