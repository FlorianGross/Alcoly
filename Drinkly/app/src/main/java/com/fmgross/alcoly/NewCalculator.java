package com.fmgross.alcoly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import com.fmgross.alcoly.backend.DatabaseHelper;
import com.fmgross.alcoly.backend.Getraenke;
import com.fmgross.alcoly.backend.GroupAdapter;
import com.fmgross.alcoly.backend.myAdapter;

import java.util.ArrayList;
import java.util.Date;

public class NewCalculator extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ArrayList<Getraenke> arrayList;
    private ArrayList<Integer> arrayListString;
    public double minResult;
    public double normalResult;
    public double highResult;
    private ImageView left, right, center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_calculator);
        left = findViewById(R.id.LeftButtonCalc);
        center = findViewById(R.id.CenterButtonCalc);
        right = findViewById(R.id.RightButtonCalc);


        getDatabase();
        createRecycler();


        left.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewCalculator.class);
            startActivity(intent);
        });
        right.setOnClickListener(v -> {
            Intent intent = new Intent(this, Statistics.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        center.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    /**
     * Returns all getränke inside an arrayList
     */
    private void getDatabase() {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();
        arrayListString = databaseHelper.getAllDates();
    }

    /**
     * Creates the recyclerview
     */
    private void createRecycler() {
        RecyclerView newRecyclerView = findViewById(R.id.mRecyclerView);
        newRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GroupAdapter groupAdapter = new GroupAdapter(this, arrayListString);
        newRecyclerView.setAdapter(groupAdapter);
        newRecyclerView.setHasFixedSize(false);
    }

    /**
     * @param context
     * @param arrayList
     * @return
     */
    public double getNormalResult(Context context, ArrayList<Getraenke> arrayList) {
        try {
            double time = getDrinkTime(arrayList, new Date());
            double promille = calculateSessionPromille(context, arrayList);
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

    /**
     * @param context
     * @return
     */
    public double getNormalResultValue(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayListHere = databaseHelper.getAllOfSession(getSessionInt(context));
        return getNormalResult(context, arrayListHere);
    }

    /**
     * @param context
     * @return
     */
    public double getNormalTimeToDrive(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayListHere = databaseHelper.getAllOfSession(getSessionInt(context));
        try {
            double promille = calculateSessionPromille(context, arrayListHere);
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

    /**
     * @param context
     * @param arrayList
     * @return
     */
    public double getMinResult(Context context, ArrayList<Getraenke> arrayList) {
        double time = getDrinkTime(arrayList, new Date());
        double promille = calculateSessionPromille(context, arrayList);
        minResult = promille - time * (0.15 / 60);
        if (minResult <= 0) {
            return 0.0;
        } else {
            return minResult;
        }
    }

    /**
     * @param context
     * @return
     */
    public double getMinResultValue(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayListHere = databaseHelper.getAllOfSession(getSessionInt(context));
        return getMinResult(context, arrayListHere);
    }

    /**
     * @param context
     * @return
     */
    public double getMinTimeToDrive(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayListHere = databaseHelper.getAllOfSession(getSessionInt(context));
        try {
            double time = getDrinkTime(arrayListHere, new Date());
            double promille = calculateSessionPromille(context, arrayListHere);
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
     * @param context
     * @param arrayList
     * @return
     */
    public double getHighResult(Context context, ArrayList<Getraenke> arrayList) {
        double time = getDrinkTime(arrayList, new Date());
        double promille = calculateSessionPromille(context, arrayList);
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
     * @param context The Context, the database gets created
     * @return The permil calculated with the high factor
     */
    public double getHighResultValue(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayListHere = databaseHelper.getAllOfSession(getSessionInt(context));
        return getHighResult(context, arrayListHere);
    }

    /**
     * Calculates the time until the permil value = 0.5
     *
     * @param context the Context, the database gets created
     * @return the time until the value = 0.5
     */
    public double getHighTimeToDrive(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayListHere = databaseHelper.getAllOfSession(getSessionInt(context));
        try {
            double time = getDrinkTime(arrayListHere, new Date());
            double promille = calculateSessionPromille(context, arrayListHere);
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
     * @param context the Activity
     * @return the session int
     */
    private int getSessionInt(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayList = databaseHelper.getAllGetraenke();
        return arrayList.get(0).getSession();
    }

    public int getNewSessionInt(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> newArrayList = databaseHelper.getAllGetraenke();
        double promille = getHighResult(context, newArrayList);
        System.out.println(newArrayList.toString());
        if (promille <= 0) {
            System.out.println("Session INT: " + newArrayList.get(0).getSession() + 1);
            return newArrayList.get(0).getSession() + 1;
        } else {
            System.out.println("Session INT: " + newArrayList.get(0).getSession());
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
    public double getDrinkTime(ArrayList<Getraenke> arrayList, Date now) {
        Date firstDrink = arrayList.get(arrayList.size() - 1).getDate();
        double result = now.getTime() - firstDrink.getTime();
        return result / 60000;
    }

    /**
     * Calculates the permil from all the drinks of one session
     *
     * @param context   the Activity
     * @param arrayList the arrayList with the session
     * @return the permil volume
     */
    public double calculateSessionPromille(Context context, ArrayList<Getraenke> arrayList) {
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

    public double calculateOnePromille(Context context, Getraenke getraenke) {
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

    private void setOnCLickListener() {
        myAdapter.RecyclerViewClickListener listener = (v, position) -> {
            Intent intent = new Intent(getApplicationContext(), Details.class);
            intent.putExtra("intPosition", position);
            startActivity(intent);
        };
    }

    public ArrayList<Integer> getDates() {
        ArrayList<Integer> dates;
        databaseHelper = new DatabaseHelper(getApplicationContext());
        dates = databaseHelper.getAllDates();
        return dates;
    }

    public double allAlcoholTogether(Context context) {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();
        double result = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            result += arrayList.get(i).getVolume();
        }
        return result;
    }

    public double getPermilInSessionToDrink(Context context, int sessionInt, Getraenke getraenk) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayListHere = databaseHelper.getAllOfSession(sessionInt);
        int i = 0;
        double permil = 0;
        do {
            permil += calculateOnePromille(context, arrayListHere.get(i));
            i++;
        } while (arrayListHere.get(i) != getraenk);

        return 0;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public int getSessionAmount(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayListHere = databaseHelper.getAllOfSession(getSessionInt(context));
        return arrayListHere.size();
    }
}

