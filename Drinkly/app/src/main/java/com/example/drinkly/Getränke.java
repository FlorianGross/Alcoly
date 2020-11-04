package com.example.drinkly;

import android.net.Uri;

import java.util.Date;

public class Getränke {
    private Uri uri;
    private Date date;
    private int volume;
    private float volumePart;

    public Getränke(Uri uri, Date date, int volume, float volumePart) {
        uri = this.uri;
        date = this.date;
        volume = this.volume;
        volumePart = this.volumePart;
    }

    public Date getDate() {
        return date;
    }

    public int getVolume() {
        return volume;
    }

    public Uri getUri() {
        return uri;
    }

    public float getVolumePart() {
        return volumePart;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setVolumePart(int volumePart) {
        this.volumePart = volumePart;
    }
}
