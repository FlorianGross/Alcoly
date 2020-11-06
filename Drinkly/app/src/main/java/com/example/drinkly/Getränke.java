package com.example.drinkly;

public class Getränke {
    private String uri;
    private long date;
    private int volume;
    private float volumePart;

    public Getränke(String uri, long date, float volume, float volumePart) {
        uri = this.uri;
        date = this.date;
        volume = this.volume;
        volumePart = this.volumePart;
    }

    @Override
    public String toString() {
        return "Getränke{" +
                "uri='" + uri + '\'' +
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

    public String getUri() {
        return uri;
    }

    public float getVolumePart() {
        return volumePart;
    }

    public void setUri(String uri) {
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
