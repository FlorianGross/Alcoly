package com.example.drinkly;

public class Getränke {
    private int countNumber;
    private String uri;
    private long date;
    private int volume;
    private float volumePart;

    public Getränke(int countNumber, String uri, long date, float volume, float volumePart) {
        countNumber = this.countNumber;
        uri = this.uri;
        date = this.date;
        volume = this.volume;
        volumePart = this.volumePart;
    }

    @Override
    public String toString() {
        return "Getränke{" +
                "countNumber=" + countNumber +
                ", uri='" + uri + '\'' +
                ", date=" + date +
                ", volume=" + volume +
                ", volumePart=" + volumePart +
                '}';
    }

    public int getCountNumber() {
        return countNumber;
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

    public void setCountNumber(int countNumber) {
        this.countNumber = countNumber;
    }
}
