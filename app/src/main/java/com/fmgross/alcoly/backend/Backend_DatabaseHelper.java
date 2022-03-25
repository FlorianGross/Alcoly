package com.fmgross.alcoly.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class Backend_DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "getraenkeSammlung";
    public static final String TABLE_NAME = "table_name";
    public static final String COLUMN_GETRRAENK_NAME = "GETRAENK_NAME";
    public static final String COLUMN_GETRAENK_URI = "GETRAENK_URI";
    public static final String COLUMN_GETRAENK_DATE = "GETRAENK_DATE";
    public static final String COLUMN_GETRAENK_VOLUME = "GETRAENK_VOLUME";
    public static final String COLUMN_GETRAENK_VOLUMEP = "GETRAENK_VOLUMEP";
    public static final String COLUMN_GETRAENK_REALDATE = "GETRAENK_REALDATE";
    public static final String COLUMN_GETRAENK_SESSION = "GETRAENK_SESSION";
    public static final String COLUMN_ID = "ID";

    public Backend_DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_GETRRAENK_NAME + " TEXT, " + COLUMN_GETRAENK_URI + " BLOB, " + COLUMN_GETRAENK_DATE + " BLOB, " + COLUMN_GETRAENK_VOLUME + " REAL, " + COLUMN_GETRAENK_VOLUMEP + " REAL, " + COLUMN_GETRAENK_REALDATE + " INTEGER, " + COLUMN_GETRAENK_SESSION + " INTEGER )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    /**
     * Adds one getränk to the Database
     *
     * @param getraenke the object to be added
     */
    public void addOne(Backend_Getraenk getraenke) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Bitmap bitmap = getraenke.getUri();
        ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, objectByteArrayOutputStream);
        byte[] imageInByte = objectByteArrayOutputStream.toByteArray();
        long dateLong = getraenke.getDate().getTime();
        cv.put(COLUMN_GETRRAENK_NAME, getraenke.getName());
        cv.put(COLUMN_GETRAENK_URI, imageInByte);
        cv.put(COLUMN_GETRAENK_DATE, dateLong);
        cv.put(COLUMN_GETRAENK_VOLUME, getraenke.getVolume());
        cv.put(COLUMN_GETRAENK_VOLUMEP, getraenke.getVolumePart());
        cv.put(COLUMN_GETRAENK_REALDATE, getraenke.getRealDate());
        cv.put(COLUMN_GETRAENK_SESSION, getraenke.getSession());
        db.insert(TABLE_NAME, null, cv);
    }

    /**
     * Generates the ArrayList from the Values in the Database
     *
     * @return ArrayList with all the elements from the Database
     */
    public ArrayList<Backend_Getraenk> getAllGetraenke() {
        ArrayList<Backend_Getraenk> getraenke = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToLast()) {
            do {
                String getraenkName = cursor.getString(1);
                byte[] getraenkUri = cursor.getBlob(2);
                long getraenkDate = cursor.getLong(3);
                float getraenkVolume = cursor.getFloat(4);
                float getraenkVolumeP = cursor.getFloat(5);
                int realDate = cursor.getInt(6);
                int session = cursor.getInt(7);
                Bitmap bitmap = BitmapFactory.decodeByteArray(getraenkUri, 0, getraenkUri.length);
                Date returnDate = new Date(getraenkDate);
                Backend_Getraenk newGetraenke = new Backend_Getraenk(getraenkName, bitmap, returnDate, getraenkVolume, getraenkVolumeP, realDate, session);
                getraenke.add(newGetraenke);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        db.close();
        return getraenke;
    }

    /**
     * Deletes one item from the Database
     *
     * @param getraenke the object, to be removed
     */
    public void deleteOne(Backend_Getraenk getraenke) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryString = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_GETRAENK_DATE + " = " + getraenke.getDate().getTime();

        Cursor cursor = database.rawQuery(queryString, null);
        cursor.moveToFirst();
        cursor.close();
    }

    /**
     * Gets all the Objects from one Day
     *
     * @param date the specific Date
     * @return An new ArrayList with all the Objects from this date
     */
    public ArrayList<Backend_Getraenk> getAllOfDate(int date) {
        ArrayList<Backend_Getraenk> getraenkeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_GETRAENK_REALDATE + " = " + date;

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToLast()) {
            do {
                String getraenkName = cursor.getString(1);
                byte[] getraenkUri = cursor.getBlob(2);
                long getraenkDate = cursor.getLong(3);
                float getraenkVolume = cursor.getFloat(4);
                float getraenkVolumeP = cursor.getFloat(5);
                int realDate = cursor.getInt(6);
                int session = cursor.getInt(7);

                Bitmap bitmap = BitmapFactory.decodeByteArray(getraenkUri, 0, getraenkUri.length);
                Date returnDate = new Date(getraenkDate);
                Backend_Getraenk newGetreanke = new Backend_Getraenk(getraenkName, bitmap, returnDate, getraenkVolume, getraenkVolumeP, realDate, session);
                getraenkeList.add(newGetreanke);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        db.close();
        return getraenkeList;
    }

    /**
     * Returns a list with all the dates, represented in the Database
     *
     * @return ArrayList with all the Dates
     */
    public ArrayList<Integer> getAllDates() {
        ArrayList<Integer> returnArray = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToLast()) {
            do {
                returnArray.add(cursor.getInt(6));
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return removeDublicates(returnArray);
    }

    /**
     * Removes the Dublicates from the getAllDates function
     *
     * @param returnArray the begin array, with the dublicates
     * @return the finalized array without the dublicates
     */
    private ArrayList<Integer> removeDublicates(ArrayList<Integer> returnArray) {
        Set<Integer> set = new LinkedHashSet<>(returnArray);
        returnArray.clear();
        returnArray.addAll(set);
        return returnArray;
    }

    /**
     * Creates an ArrayList with all the Getraenke from one Session
     *
     * @param sessionInt the number of the session
     * @return ArrayList with all the getraenke
     */
    public ArrayList<Backend_Getraenk> getAllOfSession(int sessionInt) {
        ArrayList<Backend_Getraenk> getraenkeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_GETRAENK_SESSION + " = " + sessionInt;

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToLast()) {
            do {
                String getraenkName = cursor.getString(1);
                byte[] getraenkUri = cursor.getBlob(2);
                long getraenkDate = cursor.getLong(3);
                float getraenkVolume = cursor.getFloat(4);
                float getraenkVolumeP = cursor.getFloat(5);
                int realDate = cursor.getInt(6);
                int session = cursor.getInt(7);

                Bitmap bitmap = BitmapFactory.decodeByteArray(getraenkUri, 0, getraenkUri.length);
                Date returnDate = new Date(getraenkDate);
                Backend_Getraenk newGetreanke = new Backend_Getraenk(getraenkName, bitmap, returnDate, getraenkVolume, getraenkVolumeP, realDate, session);
                getraenkeList.add(newGetreanke);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        db.close();
        return getraenkeList;
    }

    public ArrayList<Backend_Getraenk> getAllToTime() {
        ArrayList<Backend_Getraenk> getraenkeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Date date = new Date();
        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_GETRAENK_DATE + " > " + (date.getTime() - (8.64e+7/2));

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToLast()) {
            do {
                String getraenkName = cursor.getString(1);
                byte[] getraenkUri = cursor.getBlob(2);
                long getraenkDate = cursor.getLong(3);
                float getraenkVolume = cursor.getFloat(4);
                float getraenkVolumeP = cursor.getFloat(5);
                int realDate = cursor.getInt(6);
                int session = cursor.getInt(7);

                Bitmap bitmap = BitmapFactory.decodeByteArray(getraenkUri, 0, getraenkUri.length);
                Date returnDate = new Date(getraenkDate);
                Backend_Getraenk newGetreanke = new Backend_Getraenk(getraenkName, bitmap, returnDate, getraenkVolume, getraenkVolumeP, realDate, session);
                getraenkeList.add(newGetreanke);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        db.close();
        return getraenkeList;
    }
}
