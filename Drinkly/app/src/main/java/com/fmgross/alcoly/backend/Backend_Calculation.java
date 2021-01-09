package com.fmgross.alcoly.backend;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Date;

public class Backend_Calculation {
    private Backend_DatabaseHelper databaseHelper;
    private ArrayList<Backend_Getraenk> arrayList;
    private ArrayList<Integer> arrayListString;
    public double minResult;
    public double normalResult;
    public double highResult;
    private final Context context;

    public Backend_Calculation(Context context) {
        this.context = context;
    }

    public double getNormalResult(ArrayList<Backend_Getraenk> arrayList) {
        try {
            double time = getDrinkTime(arrayList, new Date());
            double promille = calculateSessionPromille(arrayList);
            normalResult = promille - time * (0.13 / 60);
            if (normalResult < 0) {
                return 0;
            } else {
                return normalResult;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public double getNormalResultToTime(ArrayList<Backend_Getraenk> arrayList, Date date) {
        try {
            double time = getDrinkTime(arrayList, date);
            double promille = calculateSessionPromille(arrayList);
            normalResult = promille - time * (0.13 / 60);
            if (normalResult < 0) {
                return 0;
            } else {
                return normalResult;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public double getNormalResultValue() {
        databaseHelper = new Backend_DatabaseHelper(context.getApplicationContext());
        ArrayList<Backend_Getraenk> arrayListHere = databaseHelper.getAllOfSession(getSessionInt());
        return getNormalResult(arrayListHere);
    }

    public double getNormalTimeToDrive() {
        databaseHelper = new Backend_DatabaseHelper(context.getApplicationContext());
        ArrayList<Backend_Getraenk> arrayListHere = databaseHelper.getAllOfSession(getSessionInt());
        try {
            double promille = calculateSessionPromille(arrayListHere);
            double returnTime = (promille - 0.5) / (0.13 / 60);
            if (returnTime < 0) {
                return 0;
            } else {
                return returnTime;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public double getMinResult(ArrayList<Backend_Getraenk> arrayList) {
        double time = getDrinkTime(arrayList, new Date());
        double promille = calculateSessionPromille(arrayList);
        minResult = promille - time * (0.15 / 60);
        if (minResult <= 0) {
            return 0.0;
        } else {
            return minResult;
        }
    }

    /**
     * returns the min result from the current context
     *
     * @return The permil calculated with the high factor
     */
    public double getMinResultValue() {
        databaseHelper = new Backend_DatabaseHelper(context.getApplicationContext());
        ArrayList<Backend_Getraenk> arrayListHere = databaseHelper.getAllOfSession(getSessionInt());
        return getMinResult(arrayListHere);
    }

    /**
     * calculates the time until the permil value is 0.5 or below
     *
     * @return
     */
    public double getMinTimeToDrive() {
        databaseHelper = new Backend_DatabaseHelper(context.getApplicationContext());
        ArrayList<Backend_Getraenk> arrayListHere = databaseHelper.getAllOfSession(getSessionInt());
        try {
            double time = getDrinkTime(arrayListHere, new Date());
            double promille = calculateSessionPromille(arrayListHere);
            normalResult = promille - time * (0.15 / 60);
            double returnTime = (promille - 0.5) / (0.15 / 60);
            if (returnTime < 0) {
                return 0;
            } else {
                return returnTime;
            }


        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    /**
     * Calculates the permil with the high subtract
     *
     * @return
     */
    public double getHighResult(ArrayList<Backend_Getraenk> arrayList) {
        double time = getDrinkTime(arrayList, new Date());
        double promille = calculateSessionPromille(arrayList);
        highResult = promille - time * (0.11 / 60);
        if (highResult <= 0) {
            return 0.0;
        } else {
            return highResult;
        }
    }

    /**
     * returns the high result from the current context
     *
     * @return The permil calculated with the high factor
     */
    public double getHighResultValue() {
        databaseHelper = new Backend_DatabaseHelper(context.getApplicationContext());
        ArrayList<Backend_Getraenk> arrayListHere = databaseHelper.getAllOfSession(getSessionInt());
        return getHighResult(arrayListHere);
    }

    /**
     * Calculates the time until the permil value = 0.5
     *
     * @return the time until the value = 0.5
     */
    public double getHighTimeToDrive() {
        databaseHelper = new Backend_DatabaseHelper(context.getApplicationContext());
        ArrayList<Backend_Getraenk> arrayListHere = databaseHelper.getAllOfSession(getSessionInt());
        try {
            double time = getDrinkTime(arrayListHere, new Date());
            double promille = calculateSessionPromille(arrayListHere);
            normalResult = promille - time * (0.11 / 60);
            double returnTime = (promille - 0.5) / (0.11 / 60);
            if (returnTime < 0) {
                return 0;
            } else {
                return returnTime;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    /**
     * Returns the Session id from the latest drink in the database
     *
     * @return the session int
     */
    private int getSessionInt() {
        databaseHelper = new Backend_DatabaseHelper(context.getApplicationContext());
        ArrayList<Backend_Getraenk> arrayList = databaseHelper.getAllGetraenke();
        return arrayList.get(0).getSession();
    }

    /**
     * creates the session int with a value higher, if the permil value is equals 0
     *
     * @return the new Session int created
     */
    public int getNewSessionInt() {
        databaseHelper = new Backend_DatabaseHelper(context.getApplicationContext());
        ArrayList<Backend_Getraenk> newArrayList = databaseHelper.getAllGetraenke();
        double promille = getHighResult(newArrayList);
        if (promille <= 0) {
            return newArrayList.get(0).getSession() + 1;
        } else {
            return newArrayList.get(0).getSession();
        }
    }

    /**
     * Calculates the Time between the First and the last drink
     *
     * @param arrayList the list of all drinks
     * @param now       the last drink of the arrayList
     * @return the duration between the first and the last drink in hours
     */
    public double getDrinkTime(ArrayList<Backend_Getraenk> arrayList, Date now) {
        Date firstDrink = arrayList.get(arrayList.size() - 1).getDate();
        double result = now.getTime() - firstDrink.getTime();
        return result / 60000;
    }

    /**
     * Calculates the permil from all the drinks of one session
     *
     * @param arrayList the arrayList with the session
     * @return the permil volume
     */
    public double calculateSessionPromille(ArrayList<Backend_Getraenk> arrayList) {
        SharedPreferences settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String gender = settings.getString("gender", "Male");
        double r = getGenderR(gender.equals("Male"));
        int age = settings.getInt("age", 20);
        double u = getAgeU(age < 55, 0.15, 0.2);
        int m = settings.getInt("weight", 80);
        double p = 0.8;
        double v;
        double e;
        double a = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            v = (arrayList.get(i).getVolume() * 1000);
            e = (arrayList.get(i).getVolumePart() / 100);
            a += v * e * p;
        }
        double value = (a / (m * r));
        return (value - (u * value));
    }

    /**
     * Calculates the permil for one of the drinks
     *
     * @param getraenke the getraenk which is calculated in
     * @return the permil for one drink
     */
    public double calculateOnePromille(Backend_Getraenk getraenke) {
        SharedPreferences settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String gender = settings.getString("gender", "Male");
        double r = getGenderR(gender.equals("Male"));
        int age = settings.getInt("age", 20);
        double u = getAgeU(age < 55, 0.15, 0.2);
        int m = settings.getInt("weight", 80);
        double p = 0.8;
        double v;
        double e;
        double a = 0;
        v = (getraenke.getVolume() * 1000);
        e = (getraenke.getVolumePart() / 100);
        a += v * e * p;
        double value = (a / (m * r));
        return (value - (u * value));
    }

    /**
     * Sets the "u" value from  the userinput from appStart
     *
     * @param b  boolean, weather its a above 55 or lower
     * @param v2 the double from the age under 55
     * @param v3 the double from the age over 55
     * @return double "u"
     */
    private double getAgeU(boolean b, double v2, double v3) {
        double u;
        if (b) {
            u = v2;
        } else {
            u = v3;
        }
        return u;
    }

    /**
     * Sets the "r" value from the userInput from Appstart
     *
     * @param male Boolean weather the person is Male or Female
     * @return the "r" value
     */
    private double getGenderR(boolean male) {
        return getAgeU(male, 0.68, 0.55);
    }

    /**
     * Creates an ArrayList with all the Dates
     *
     * @return
     */
    public ArrayList<Integer> getDates() {
        ArrayList<Integer> dates;
        databaseHelper = new Backend_DatabaseHelper(context.getApplicationContext());
        dates = databaseHelper.getAllDates();
        return dates;
    }

    /**
     * Calculates all the values together
     *
     * @return all volumes together
     */
    public double allAlcoholTogether() {
        databaseHelper = new Backend_DatabaseHelper(context.getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();
        double result = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            result += arrayList.get(i).getVolume();
        }
        return result;
    }

    /**
     * @param sessionInt
     * @param getraenk
     * @return
     */
    public double getPermilInSessionToDrink(int sessionInt, Backend_Getraenk getraenk) {
        databaseHelper = new Backend_DatabaseHelper(context.getApplicationContext());
        ArrayList<Backend_Getraenk> arrayListHere = databaseHelper.getAllOfSession(sessionInt);
        int i = 0;
        double permil = 0;
        do {
            permil += calculateOnePromille(arrayListHere.get(i));
            i++;
        } while (arrayListHere.get(i) != getraenk);

        return 0;
    }

    /**
     * Returns the size of the ArrayList with all the drinks with one session
     *
     * @return the amount of Getraenke in the session
     */
    public int getSessionAmount() {
        databaseHelper = new Backend_DatabaseHelper(context.getApplicationContext());
        ArrayList<Backend_Getraenk> arrayListHere = databaseHelper.getAllOfSession(getSessionInt());
        return arrayListHere.size();
    }
}
