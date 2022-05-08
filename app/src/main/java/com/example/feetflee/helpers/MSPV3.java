package com.example.feetflee.helpers;


import android.content.Context;
import android.content.SharedPreferences;

public class MSPV3 {
    private final String SP_FILE = "SP_FILE";


    private static MSPV3 instance;
    private SharedPreferences sharedPreferences;

    public static MSPV3 getInstance(Context context) {
        if (instance == null)
            instance = new MSPV3(context);

        return instance;
    }

    public static MSPV3 getInstance() {
        return instance;
    }

    private MSPV3(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }


    public void putInt(String KEY, int value) {
        sharedPreferences.edit().putInt(KEY, value).apply();
    }
    public int getInt(String KEY, int defValue) {
        return sharedPreferences.getInt(KEY,0);
    }

    public String getString(String KEY, String defValue) {
        return sharedPreferences.getString(KEY, defValue);
    }

    public void putString(String KEY, String value) {
        sharedPreferences.edit().putString(KEY, value).apply();
    }

}