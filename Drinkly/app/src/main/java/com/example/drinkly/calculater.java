package com.example.drinkly;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class calculater extends Service {
    public calculater() {
    }


    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}