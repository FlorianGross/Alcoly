package com.example.drinkly;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.app.NotificationCompat;

import com.example.drinkly.NonMain.DatabaseHelper;
import com.example.drinkly.NonMain.Getränke;
import com.example.drinkly.NonMain.GroupAdapter;
import com.example.drinkly.NonMain.myAdapter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class calculator extends Service {


    private DatabaseHelper databaseHelper;
    private ArrayList<Getränke> arrayList;
    private ArrayList<Integer> arrayListString;
    public double minResult;
    public double normalResult;
    public double highResult;
    private Date firstDrink;
    private Date lastDrink;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotification();

        while(highResult > 0){
            refreshData();
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return START_STICKY;
    }

    private void refreshData() {

    }

    private void createNotification() {
        createNotificationChanel();

        Intent intent1 = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        Notification notification = new NotificationCompat.Builder(
                this, "ChannelId1").setContentTitle("Alcoly").setContentText("Promillewert wird berechnet").setSmallIcon(R.drawable.alcoly_logo).setContentIntent(pendingIntent).build();
        startForeground(1, notification);
    }

    private void createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "ChannelId1", "Foreground notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }


    public float getMinPermilAtTime(Context context, long date) {
        getDatabase(context);
        Date dateDate = new Date(date);
        double promille = getPromilleToDate(context, dateDate);
        double time = getDrinkTime(arrayList, dateDate);
        return (float) (promille - time * (0.15 / 60));
    }


    public float getMedPermilAtTime(Context context, long date) {
        getDatabase(context);
        Date dateDate = new Date(date);
        double promille = getPromilleToDate(context, dateDate);
        double time = getDrinkTime(arrayList, dateDate);
        return (float) (promille - time * (0.13 / 60));
    }


    public float getMaxPermilAtTime(Context context, long date) {
        Date dateDate = new Date(date);
        double promille = getPromilleToDate(context, dateDate);
        double time = getDrinkTime(arrayList, dateDate);
        return (float) (promille - time * (0.11 / 60));
    }

    private double getPromilleToDate(Context context, Date dateDate) {
        getDatabase(context);
        int lastElement = getLastElementWithDate(dateDate);
        return calculatePromille(context, arrayList, sessionStart(arrayList.get(arrayList.size() - 1).getDate(), arrayList), lastElement);
    }

    private int getLastElementWithDate(Date dateDate) {
        int iterator = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getDate().getTime() < dateDate.getTime()) {
                iterator = i + 1;
            }
        }
        return iterator;
    }

    private void getDatabase(Context context) {
        databaseHelper = new DatabaseHelper(context);
        arrayList = databaseHelper.getAllGetraenke();
        arrayListString = databaseHelper.getAllDates(arrayList);
    }

    /**
     * Calculates 3 possible versions of the permile value
     *
     * @param arrayList the list of all drinks
     */
    public void getResult(Context context, ArrayList<Getränke> arrayList) {
        lastDrink = arrayList.get(arrayList.size() - 1).getDate();
        double time = getDrinkTime(arrayList, new Date());
        double promille = calculatePromille(context, arrayList, sessionStart(lastDrink, arrayList), arrayList.size());
        minResult = promille - time * (0.15 / 60);
        normalResult = promille - time * (0.13 / 60);
        highResult = promille - time * (0.11 / 60);
        if (minResult <= 0) {
            minResult = 0;
        }
        if (normalResult <= 0) {
            normalResult = 0;
        }
        if (highResult <= 0) {
            highResult = 0;
        }
    }

    public double getNormalResult(Context context, ArrayList<Getränke> arrayList) {
        lastDrink = arrayList.get(arrayList.size() - 1).getDate();
        double time = getDrinkTime(arrayList, new Date());
        double promille = calculatePromille(context, arrayList, sessionStart(lastDrink, arrayList), arrayList.size());
        normalResult = promille - time * (0.13 / 60);
        if (minResult <= 0) {
            return 0.0;
        }else{
            return normalResult;
        }
    }

    public double getMinResult(Context context, ArrayList<Getränke> arrayList) {
        lastDrink = arrayList.get(arrayList.size() - 1).getDate();
        double time = getDrinkTime(arrayList, new Date());
        double promille = calculatePromille(context, arrayList, sessionStart(lastDrink, arrayList), arrayList.size());
        minResult = promille - time * (0.15 / 60);
        if (minResult <= 0) {
            return 0.0;
        }else{
            return minResult;
        }
    }

    public double getHighResult(Context context, ArrayList<Getränke> arrayList) {
        lastDrink = arrayList.get(arrayList.size() - 1).getDate();
        double time = getDrinkTime(arrayList, new Date());
        double promille = calculatePromille(context, arrayList, sessionStart(lastDrink, arrayList), arrayList.size());
        highResult = promille - time * (0.11 / 60);
        if (minResult <= 0) {
            return 0.0;
        }else{
            return highResult;
        }
    }

    /**
     * Calculates the Time between the First and the last drink
     *
     * @param arrayList the list of all drinks
     * @param now       the last drink of the arrayList
     * @return the duration between the first and the last drink in hours
     */
    public double getDrinkTime(ArrayList<Getränke> arrayList, Date now) {

        firstDrink = arrayList.get(sessionStart(lastDrink, arrayList)).getDate();
        double result = now.getTime() - firstDrink.getTime();
        return result / 60000;
    }

    /**
     * calculates the first drink
     *
     * @param lastDrink the last element of the arrayList
     * @param arrayList the list of all drinks
     * @return the position of the first element of the session
     */
    private int sessionStart(Date lastDrink, ArrayList<Getränke> arrayList) {
        long datelast = lastDrink.getTime();
        long dateTest;

        int returnInt = -1;
        for (int j = 0; j < arrayList.size(); j++) {
            dateTest = arrayList.get(j).getDate().getTime();
            if ((dateTest - datelast) > 8.64e+7) {
            } else if (returnInt == -1) {
                returnInt = j;
            }
        }
        return returnInt;
    }

    /**
     * Converts the Date to the Local Date
     *
     * @param dateToConvert the Date
     * @return the Date in LocalDate format
     */
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * The main Calculator
     *
     * @param arrayList    the list of all drinks
     * @param startElement the first drink of the session
     * @return permile value of the drank drinks
     */
    public double calculatePromille(Context context, ArrayList<Getränke> arrayList, int startElement, int endelement) {
        SharedPreferences settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String gender = settings.getString("gender", "Male");
        double r = getGenderR(gender.equals("Male"), 0.68, 0.55);
        int age = settings.getInt("age", 20);
        double u = getAgeU(age < 55, 0.15, 0.2);
        int m = settings.getInt("weight", 80);
        double p = 0.8;
        double v;
        double e;
        double a = 0;

        for (int i = startElement; i < endelement; i++) {
            v = (arrayList.get(i).getVolume() * 1000);
            e = (arrayList.get(i).getVolumePart() / 100);
            a += v * e * p;
        }
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
     * @param v2   the Value for Male
     * @param v3   the Value for Female
     * @return the "r" value
     */
    private double getGenderR(boolean male, double v2, double v3) {
        double r = getAgeU(male, v2, v3);
        return r;
    }

    public double getNormalResult(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getränke> arrayListHere = databaseHelper.getAllGetraenke();
        return getNormalResult(context, arrayListHere);
    }

}