package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.drinkly.backend.DatabaseHelper;
import com.example.drinkly.backend.Getraenke;
import com.example.drinkly.backend.GroupAdapter;
import com.example.drinkly.backend.myAdapter;

import java.util.ArrayList;
import java.util.Date;

public class NewCalculator extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ArrayList<Getraenke> arrayList;
    private ArrayList<Integer> arrayListString;
    public double minResult;
    public double normalResult;
    public double highResult;
    private Date lastDrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_calculator);
        //Reads the Database and creates the Arraylist
        getDatabase();
        //Creates the nested Recyclerview
        createRecycler();
    }

    private void getDatabase() {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();
        arrayListString = databaseHelper.getAllDates(arrayList);
    }

    private void createRecycler() {
        RecyclerView newRecyclerView = findViewById(R.id.mRecyclerView);
        newRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GroupAdapter groupAdapter = new GroupAdapter(this, arrayListString);
        newRecyclerView.setAdapter(groupAdapter);
        newRecyclerView.setHasFixedSize(false);
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

    public double getNormalResult(Context context, ArrayList<Getraenke> arrayList) {
        try {
            lastDrink = arrayList.get(arrayList.size() - 1).getDate();
            double time = getDrinkTime(arrayList, new Date());
            double promille = calculatePromille(context, arrayList, sessionStart(lastDrink, arrayList), arrayList.size());
            normalResult = promille - time * (0.13 / 60);
            System.out.println(time + "Time");
            System.out.println(promille + " promille");
            if (normalResult < 0) {
                return 0;
            } else {
                return normalResult;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public double getNormalResultValue(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayListHere = databaseHelper.getAllGetraenke();
        return getNormalResult(context, arrayListHere);
    }

    public double getNormalTimeToDrive(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayListHere = databaseHelper.getAllGetraenke();
        try {
            lastDrink = arrayListHere.get(arrayListHere.size() - 1).getDate();
            double time = getDrinkTime(arrayListHere, new Date());
            double promille = calculatePromille(context, arrayListHere, sessionStart(lastDrink, arrayListHere), arrayListHere.size());
            normalResult = promille - time * (0.13 / 60);
            return (promille) / (0.13 / 60);
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public double getMinResult(Context context, ArrayList<Getraenke> arrayList) {
        lastDrink = arrayList.get(arrayList.size() - 1).getDate();
        double time = getDrinkTime(arrayList, new Date());
        double promille = calculatePromille(context, arrayList, sessionStart(lastDrink, arrayList), arrayList.size());
        minResult = promille - time * (0.15 / 60);
        if (minResult <= 0) {
            return 0.0;
        } else {
            return minResult;
        }
    }

    public double getMinResultValue(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayListHere = databaseHelper.getAllGetraenke();
        return getMinResult(context, arrayListHere);
    }

    public double getMinTimeToDrive(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayListHere = databaseHelper.getAllGetraenke();
        try {
            lastDrink = arrayListHere.get(arrayListHere.size() - 1).getDate();
            double time = getDrinkTime(arrayListHere, new Date());
            double promille = calculatePromille(context, arrayListHere, sessionStart(lastDrink, arrayListHere), arrayListHere.size());
            normalResult = promille - time * (0.15 / 60);
            return (promille) / (0.15 / 60);
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public double getHighResult(Context context, ArrayList<Getraenke> arrayList) {
        lastDrink = arrayList.get(arrayList.size() - 1).getDate();
        double time = getDrinkTime(arrayList, new Date());
        double promille = calculatePromille(context, arrayList, sessionStart(lastDrink, arrayList), arrayList.size());
        highResult = promille - time * (0.11 / 60);
        if (highResult <= 0) {
            return 0.0;
        } else {
            return highResult;
        }
    }

    public double getHighResultValue(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayListHere = databaseHelper.getAllGetraenke();
        return getHighResult(context, arrayListHere);
    }

    public double getHighTimeToDrive(Context context) {
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        ArrayList<Getraenke> arrayListHere = databaseHelper.getAllGetraenke();
        try {
            lastDrink = arrayListHere.get(arrayListHere.size() - 1).getDate();
            double time = getDrinkTime(arrayListHere, new Date());
            double promille = calculatePromille(context, arrayListHere, sessionStart(lastDrink, arrayListHere), arrayListHere.size());
            normalResult = promille - time * (0.11 / 60);
            return (promille) / (0.11 / 60);
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
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

        Date firstDrink = arrayList.get(sessionStart(lastDrink, arrayList)).getDate();
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
    private int sessionStart(Date lastDrink, ArrayList<Getraenke> arrayList) {
        long datelast = lastDrink.getTime();
        long dateTest;
        int returnInt = -1;
        for (int j = 0; j < arrayList.size(); j++) {
            dateTest = arrayList.get(j).getDate().getTime();
            if ((datelast - dateTest) > 4.32e+7) {
            } else if (returnInt == -1) {
                returnInt = j;
            }
        }
        return returnInt;
    }

    /**
     * The main Calculator
     *
     * @param arrayList    the list of all drinks
     * @param startElement the first drink of the session
     * @return permile value of the drank drinks
     */
    public double calculatePromille(Context context, ArrayList<Getraenke> arrayList, int startElement, int endelement) {
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
     * @return the "r" value
     */
    private double getGenderR(boolean male) {
        return getAgeU(male, 0.68, 0.55);
    }

    /**
     * Sets the OnClickListener for the detailsview
     */
    private void setOnCLickListener() {
        myAdapter.RecyclerViewClickListener listener = (v, position) -> {
            Intent intent = new Intent(getApplicationContext(), Details.class);
            intent.putExtra("intPosition", position);
            startActivity(intent);
        };
    }
}

