package com.example.drinkly.NonMain;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.drinkly.NonMain.Getränke;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PrefConfig {
    private static final String LIST_KEY = "list_key";

    /**
     * Writes the Gson inside the ArrayList
     *
     * @param context the activity, the function is called in
     * @param drinks  the ArrayList, where the Gson is stored inside
     */
    public static void writeListInPref(Context context, ArrayList<Getränke> drinks) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(drinks);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LIST_KEY, jsonString);
        editor.apply();
    }

    /**
     * Reads the values from the SharedPreference
     *
     * @param context the Activity, the function is called in
     * @return the ArrayList with the data from the Shared preferences
     */
    public static ArrayList<Getränke> readListFromPref(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString(LIST_KEY, "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Getränke>>() {
        }.getType();
        ArrayList<Getränke> drinks = gson.fromJson(jsonString, type);
        return drinks;
    }
}
