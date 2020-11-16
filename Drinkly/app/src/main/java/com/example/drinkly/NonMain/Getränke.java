package com.example.drinkly.NonMain;

import android.graphics.Bitmap;

public class Getränke {
    private Bitmap uri;
    private long date;
    private float volume;
    private float volumePart;

    public Getränke(Bitmap uri, long date, float volume, float volumePart) {
        this.uri = uri;
        this.date = date;
        this.volume = volume;
        this.volumePart = volumePart;

    }

    @Override
    public String toString() {
        return "Getränke{" +
                "Bitmap='" + uri.toString() +
                ", date=" + date +
                ", volume=" + volume +
                ", volumePart=" + volumePart +
                '}';
    }


    public long getDate() {
        return date;
    }

    public float getVolume() {
        return volume;
    }

    public Bitmap getUri() {
        return uri;
    }

    public float getVolumePart() {
        return volumePart;
    }

    public void setUri(Bitmap uri) {
        this.uri = uri;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setVolumePart(int volumePart) {
        this.volumePart = volumePart;
    }

}
