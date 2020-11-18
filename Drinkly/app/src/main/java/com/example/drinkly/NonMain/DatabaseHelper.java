package com.example.drinkly.NonMain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.drinkly.NonMain.Getränke;
import com.example.drinkly.oldClass.Calculator;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInByte;
    private long dateLong;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_GETTRAENK_URI + " BLOB, " + COLUMN_GETRAENK_DATE + " BLOB, " + COLUMN_GETRAENK_VOLUME + " REAL, " + COLUMN_GETRAENK_VOLUMEP + " REAL, " + COLUMN_GETRAENK_REALDATE + " TEXT )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean addOne(Getränke getränke) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Bitmap bitmap = getränke.getUri();
        objectByteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, objectByteArrayOutputStream);
        imageInByte = objectByteArrayOutputStream.toByteArray();
        dateLong = getränke.getDate().getTime();
        cv.put(COLUMN_GETTRAENK_URI, imageInByte);
        cv.put(COLUMN_GETRAENK_DATE, dateLong);
        cv.put(COLUMN_GETRAENK_VOLUME, getränke.getVolume());
        cv.put(COLUMN_GETRAENK_VOLUMEP, getränke.getVolumePart());
        cv.put(COLUMN_GETRAENK_REALDATE, getränke.getRealDate());
        long insert = db.insert(TABLE_NAME, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<Getränke> getAllGetraenke() {
        ArrayList<Getränke> getränke = new ArrayList<Getränke>();
        String queryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToLast()) {
            do {
                byte[] getraenkUri = cursor.getBlob(0);
                long getraenkDate = cursor.getLong(1);
                float getraenkVolume = cursor.getFloat(2);
                float getraenkVolumeP = cursor.getFloat(3);
                String realDate = cursor.getString(4);

                Bitmap bitmap = BitmapFactory.decodeByteArray(getraenkUri, 0, getraenkUri.length);
                Date returnDate = new Date(getraenkDate);
                Getränke newGetränke = new Getränke(bitmap, returnDate, getraenkVolume, getraenkVolumeP, realDate);
                getränke.add(newGetränke);
            } while (cursor.moveToPrevious());
        } else {

        }
        cursor.close();
        db.close();
        return getränke;
    }

    public boolean deleteOne(Getränke getränke) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryString = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_GETTRAENK_URI + " = " + getränke.getUri();

        Cursor cursor = database.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteAllGetränke() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        return true;
    }

    public ArrayList<Getränke> getAllOfDate(String date) {
        ArrayList<Getränke> getränkeList = new ArrayList<Getränke>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT FROM " + TABLE_NAME + " WHERE " + COLUMN_GETRAENK_REALDATE + " LIKE " + date;

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToLast()) {
            do {
                byte[] getraenkUri = cursor.getBlob(0);
                long getraenkDate = cursor.getLong(1);
                float getraenkVolume = cursor.getFloat(2);
                float getraenkVolumeP = cursor.getFloat(3);
                String realDate = cursor.getString(4);

                Bitmap bitmap = BitmapFactory.decodeByteArray(getraenkUri, 0, getraenkUri.length);
                Date returnDate = new Date(dateLong);
                Getränke newGetränke = new Getränke(bitmap, returnDate, getraenkVolume, getraenkVolumeP, realDate);
                getränkeList.add(newGetränke);
            } while (cursor.moveToPrevious());
        } else {

        }
        cursor.close();
        db.close();
        return getränkeList;
    }

    public ArrayList<String> getAllDates(ArrayList<Getränke> getränkeList) {
        ArrayList<String> returnArray = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToLast()) {
            do {
                returnArray.add(cursor.getString(4));
            } while (cursor.moveToPrevious());
        }


        returnArray = removeDublicates(returnArray);

        return returnArray;
    }

    private ArrayList<String> removeDublicates(ArrayList<String> returnArray) {
    Set<String> set = new LinkedHashSet<>();
    set.addAll(returnArray);
    returnArray.clear();
    returnArray.addAll(set);
    return returnArray;
    }

}
