package com.example.feetflee;
import android.app.Application;

import com.example.feetflee.helpers.MSPV3;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MSPV3.getInstance(this);
    }
}