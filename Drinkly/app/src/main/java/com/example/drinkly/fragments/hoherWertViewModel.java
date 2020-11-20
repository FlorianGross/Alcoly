package com.example.drinkly.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class hoherWertViewModel extends ViewModel {
    private MutableLiveData<String> timeToDrive, amountOfAlc, textType;

    public  hoherWertViewModel(){
        timeToDrive = new MutableLiveData<>();
        amountOfAlc = new MutableLiveData<>();
        textType = new MutableLiveData<>();

        timeToDrive.setValue("04:12 h");
        amountOfAlc.setValue("0,003 ml");
        textType.setValue("36 alkoholische Getränke");
    }



    public LiveData<String> getTimeToDrive() {
        return timeToDrive;
    }
    public LiveData<String> getAmountOfAlc() {
        return amountOfAlc;
    }
    public LiveData<String> getTextType() {
        return textType;
    }

}
