package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.drinkly.NonMain.DatabaseHelper;
import com.example.drinkly.NonMain.Getränke;
import com.example.drinkly.NonMain.GroupAdapter;
import com.example.drinkly.NonMain.myAdapter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class NewCalculator extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ArrayList<Getränke> arrayList;
    private GroupAdapter groupAdapter;
    private ArrayList<Integer> arrayListString;
    private myAdapter.RecyclerViewClickListener listener;
    public double minResult;
    public double normalResult;
    public double highResult;
    private Date firstDrink;
    private Date lastDrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_calculator);
        //Reads the Database and creates the Arraylist
        getDatabase();
        //Creates the nested Recyclerview
        createRecycler();
        //calculates the Results from the given Values
        getResult(arrayList);

    }

    public float getMinPermilAtTime(long date) {
        getDatabase();
        Date dateDate = new Date(date);
        double promille = getPromilleToDate(dateDate);
        double time = getDrinkTime(arrayList, dateDate);
        return (float) (promille - time * (0.15 / 60));
    }


    public float getMedPermilAtTime(long date) {
        getDatabase();
        Date dateDate = new Date(date);
        double promille = getPromilleToDate(dateDate);
        double time = getDrinkTime(arrayList, dateDate);
        return (float) (promille - time * (0.13 / 60));
    }


    public float getMaxPermilAtTime(long date) {
        Date dateDate = new Date(date);
        double promille = getPromilleToDate(dateDate);
        double time = getDrinkTime(arrayList, dateDate);
        return (float) (promille - time * (0.11 / 60));
    }

    private double getPromilleToDate(Date dateDate) {
        getDatabase();
        int lastElement = getLastElementWithDate(dateDate);
        return calculatePromille(arrayList, sessionStart(arrayList.get(arrayList.size() - 1).getDate(), arrayList), lastElement);
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

    private void getDatabase() {
        databaseHelper = new DatabaseHelper(getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();
        arrayListString = databaseHelper.getAllDates(arrayList);
    }

    private void createRecycler() {
        RecyclerView newRecyclerView = findViewById(R.id.mRecyclerView);
        newRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupAdapter = new GroupAdapter(this, arrayListString);
        newRecyclerView.setAdapter(groupAdapter);
        newRecyclerView.setHasFixedSize(false);
    }

    /**
     * Calculates 3 possible versions of the permile value
     *
     * @param arrayList the list of all drinks
     */
    public void getResult(ArrayList<Getränke> arrayList) {
        lastDrink = arrayList.get(arrayList.size() - 1).getDate();
        double time = getDrinkTime(arrayList, new Date());
        double promille = calculatePromille(arrayList, sessionStart(lastDrink, arrayList), arrayList.size());
        System.out.println(time + " . " + promille);
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
        System.out.println(normalResult);
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
    public double calculatePromille(ArrayList<Getränke> arrayList, int startElement, int endelement) {
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
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

    /**
     * Sets the OnClickListener for the detailsview
     */
    private void setOnCLickListener() {
        listener = new myAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), Details.class);
                intent.putExtra("intPosition", position);
                startActivity(intent);
            }
        };
    }

    /**
     * Creates a new Arraylist with all the dates of the current session
     *
     * @return ArrayList<Long> with all dates in long format
     */
    public ArrayList<Long> getDates() {
        ArrayList<Long> allDates = new ArrayList<>();
        for (int i = sessionStart(new Date(), arrayList); i < arrayList.size(); i++) {
            allDates.add(arrayList.get(i).getDate().getTime());
        }
        return allDates;
    }
}

