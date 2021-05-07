package com.fmgross.alcoly.backend;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Backend_Getraenk {
    private Bitmap uri;
    private int realDate;
    private Date date;
    private float volume;
    private float volumePart;
    private int session;
    private String name;

    public Backend_Getraenk(String name, Bitmap uri, Date date, float volume, float volumePart, int realDate, int session) {
        this.uri = uri;
        this.date = date;
        this.volume = volume;
        this.volumePart = volumePart;
        this.realDate = realDate;
        this.session = session;
        this.name = name;
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
                "Name=" + name +
                "Bitmap=" + uri.toString() +
                ", date=" + date.toString() +
                ", realDate=" + realDate +
                ", volume=" + volume +
                ", volumePart=" + volumePart +
                ", session=" + session +
                "}";
    }

    public String getName() {return name;}

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

    public int getSession() {
        return session;
    }

    public void setName(String name) {this.name = name;}

    public void setUri(Bitmap uri) {
        this.uri = uri;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setVolumePart(float volumePart) {
        this.volumePart = volumePart;
    }

    public void setRealDate(int realDate) {
        this.realDate = realDate;
    }

    public void setSession(int session) {
        this.session = session;
    }
}
