package com.example.drinkly.NonMain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.drinkly.NonMain.Getränke;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "getraenkeSammlung";
    public static final String TABLE_NAME = "table_name";
    public static final String COLUMN_GETTRAENK_URI = "GETTRAENK_URI";
    public static final String COLUMN_GETRAENK_DATE = "GETRAENK_DATE";
    public static final String COLUMN_GETRAENK_VOLUME = "GETRAENK_VOLUME";
    public static final String COLUMN_GETRAENK_VOLUMEP = "GETRAENK_VOLUMEP";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_GETTRAENK_URI + " TEXT, " + COLUMN_GETRAENK_DATE + " TEXT, " + COLUMN_GETRAENK_VOLUME + " REAL, " + COLUMN_GETRAENK_VOLUMEP + " REAL )";
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
        cv.put(COLUMN_GETTRAENK_URI, getränke.getUri());
        cv.put(COLUMN_GETRAENK_DATE, getränke.getDate());
        cv.put(COLUMN_GETRAENK_VOLUME, getränke.getVolume());
        cv.put(COLUMN_GETRAENK_VOLUMEP, getränke.getVolumePart());
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
        if (cursor.moveToFirst()) {
            do {
                String getraenkUri = cursor.getString(0);
                long getraenkDate = cursor.getLong(1);
                float getraenkVolume = cursor.getFloat(2);
                float getraenkVolumeP = cursor.getFloat(3);

                Getränke newGetränke = new Getränke(getraenkUri, getraenkDate, getraenkVolume, getraenkVolumeP);
                getränke.add(newGetränke);
            } while (cursor.moveToNext());
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

}