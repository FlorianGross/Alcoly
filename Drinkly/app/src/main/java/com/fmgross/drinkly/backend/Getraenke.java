package com.fmgross.drinkly.backend;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Getraenke {
    private Bitmap uri;
    private int realDate;
    private Date date;
    private float volume;
    private float volumePart;

    public Getraenke(Bitmap uri, Date date, float volume, float volumePart, int realDate) {
        this.uri = uri;
        this.date = date;
        this.volume = volume;
        this.volumePart = volumePart;
        this.realDate = realDate;
    }

    /**
     * Generates toString for the object
     *
     * @return String with all values from the object
     */
    @NotNull
    @Override
    public String toString() {
        return "Getränke{" +
                "Bitmap='" + uri.toString() +
                ", date=" + date.toString() +
                ", realDate=" + realDate +
                ", volume=" + volume +
                ", volumePart=" + volumePart +
                '}';
    }

    /**
     * Returns the Date of the object
     *
     * @return Date from the Object
     */
    public Date getDate() {
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

    public int getRealDate() {
        return realDate;
    }

    public void setUri(Bitmap uri) {
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

    public void setRealDate(int realDate) {
        this.realDate = realDate;
    }
}
