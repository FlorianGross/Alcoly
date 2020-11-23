package com.example.drinkly.backend;

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

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "getraenkeSammlung";
    public static final String TABLE_NAME = "table_name";
    public static final String COLUMN_GETTRAENK_URI = "GETRAENK_URI";
    public static final String COLUMN_GETRAENK_DATE = "GETRAENK_DATE";
    public static final String COLUMN_GETRAENK_VOLUME = "GETRAENK_VOLUME";
    public static final String COLUMN_GETRAENK_VOLUMEP = "GETRAENK_VOLUMEP";
    public static final String COLUMN_GETRAENK_REALDATE = "GETRAENK_REALDATE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_GETTRAENK_URI + " BLOB, " + COLUMN_GETRAENK_DATE + " BLOB, " + COLUMN_GETRAENK_VOLUME + " REAL, " + COLUMN_GETRAENK_VOLUMEP + " REAL, " + COLUMN_GETRAENK_REALDATE + " INTEGER )";
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
     * @return true if it succeded
     */
    public boolean addOne(Getraenke getraenke) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Bitmap bitmap = getraenke.getUri();
        ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, objectByteArrayOutputStream);
        byte[] imageInByte = objectByteArrayOutputStream.toByteArray();
        long dateLong = getraenke.getDate().getTime();
        cv.put(COLUMN_GETTRAENK_URI, imageInByte);
        cv.put(COLUMN_GETRAENK_DATE, dateLong);
        cv.put(COLUMN_GETRAENK_VOLUME, getraenke.getVolume());
        cv.put(COLUMN_GETRAENK_VOLUMEP, getraenke.getVolumePart());
        cv.put(COLUMN_GETRAENK_REALDATE, getraenke.getRealDate());
        long insert = db.insert(TABLE_NAME, null, cv);
        return insert != -1;
    }

    /**
     * Generates the ArrayList from the Values in the Database
     *
     * @return ArrayList with all the elements from the Database
     */
    public ArrayList<Getraenke> getAllGetraenke() {
        ArrayList<Getraenke> getraenke = new ArrayList<Getraenke>();
        String queryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToLast()) {
            do {
                byte[] getraenkUri = cursor.getBlob(0);
                long getraenkDate = cursor.getLong(1);
                float getraenkVolume = cursor.getFloat(2);
                float getraenkVolumeP = cursor.getFloat(3);
                int realDate = cursor.getInt(4);

                Bitmap bitmap = BitmapFactory.decodeByteArray(getraenkUri, 0, getraenkUri.length);
                Date returnDate = new Date(getraenkDate);
                Getraenke newGetraenke = new Getraenke(bitmap, returnDate, getraenkVolume, getraenkVolumeP, realDate);
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
     * @return true if it succeded
     */
    public boolean deleteOne(Getraenke getraenke) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryString = "DELETE * FROM " + TABLE_NAME + " WHERE " + COLUMN_GETTRAENK_URI + " = " + getraenke.getUri();

        Cursor cursor = database.rawQuery(queryString, null);
        return cursor.moveToFirst();
    }

    /**
     * Clears the Database from all objects
     *
     * @return true if it succeded
     */
    public boolean deleteAllGetraenke() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        return true;
    }

    /**
     * Gets all the Objects from one Day
     *
     * @param date the specific Date
     * @return An new ArrayList with all the Objects from this date
     */
    public ArrayList<Getraenke> getAllOfDate(int date) {
        ArrayList<Getraenke> getraenkeList = new ArrayList<Getraenke>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_GETRAENK_REALDATE + " == " + date;

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToLast()) {
            do {
                byte[] getraenkUri = cursor.getBlob(0);
                long getraenkDate = cursor.getLong(1);
                float getraenkVolume = cursor.getFloat(2);
                float getraenkVolumeP = cursor.getFloat(3);
                int realDate = cursor.getInt(4);

                Bitmap bitmap = BitmapFactory.decodeByteArray(getraenkUri, 0, getraenkUri.length);
                Date returnDate = new Date(getraenkDate);
                Getraenke newGetreanke = new Getraenke(bitmap, returnDate, getraenkVolume, getraenkVolumeP, realDate);
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
     * @param getraenkeList the list with all the objects from the Database
     * @return ArrayList with all the Dates
     */
    public ArrayList<Integer> getAllDates(ArrayList<Getraenke> getraenkeList) {
        ArrayList<Integer> returnArray = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToLast()) {
            do {
                returnArray.add(cursor.getInt(4));
            } while (cursor.moveToPrevious());
        }


        returnArray = removeDublicates(returnArray);

        return returnArray;
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

}