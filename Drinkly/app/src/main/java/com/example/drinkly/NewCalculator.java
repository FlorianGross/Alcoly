package com.example.drinkly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
        double time = getDrinkTime(arrayList, lastDrink);
        double promille = calculatePromille(arrayList, sessionStart(lastDrink, arrayList));
        minResult = promille - time * 0.7;
        normalResult = promille - time * 0.6;
        highResult = promille - time * 0.5;
    }

    /**
     * Calculates the Time between the First and the last drink
     *
     * @param arrayList the list of all drinks
     * @param lastDrink the last drink of the arrayList
     * @return the duration between the first and the last drink in hours
     */
    public long getDrinkTime(ArrayList<Getränke> arrayList, Date lastDrink) {

        firstDrink = arrayList.get(sessionStart(lastDrink, arrayList)).getDate();
        LocalDate firstDrinkLocal = convertToLocalDateViaInstant(firstDrink);
        LocalDate lastDrinkLocal = convertToLocalDateViaInstant(lastDrink);
        return Duration.between(firstDrinkLocal, lastDrinkLocal).toHours();
    }

    /**
     * calculates the first drink
     *
     * @param lastDrink the last element of the arrayList
     * @param arrayList the list of all drinks
     * @return the position of the first element of the session
     */
    private int sessionStart(Date lastDrink, ArrayList<Getränke> arrayList) {
        LocalDate realLastDrink = convertToLocalDateViaInstant(lastDrink);
        LocalDate testLocalDate;
        int returnInt = 0;
        for (int j = 0; j < arrayList.size(); j++) {
            testLocalDate = convertToLocalDateViaInstant(arrayList.get(j).getDate());
            if (Duration.between(realLastDrink, testLocalDate).toHours() > 24 || returnInt != 0) {
            } else {
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
    public double calculatePromille(ArrayList<Getränke> arrayList, int startElement) {
        SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        String gender = settings.getString("gender", "Male");
        double r = getGenderR(gender.equals("Male"), 0.68, 0.55);
        int age = settings.getInt("age", 20);
        double u = getAgeU(age < 55, 0.15, 0.2);
        int m = settings.getInt("weight", 80);
        double p = 0.8;
        double v = 0;
        double e = 0;


        for (int i = startElement; i < arrayList.size(); i++) {
            v += arrayList.get(i).getVolume();
            e += arrayList.get(i).getVolumePart();

        }

        return ((v * e * p) / (m * r) - u);
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

}



        /*
        databaseHelper = new DatabaseHelper(getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();

        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        int numberOfColumns = 3;

        setOnCLickListener();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new myAdapter(this, arrayList, listener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(false);
*/

