package com.example.feetflee.helpers;

import android.content.Context;

public class LocationClass {

    //location
    private GpsTracker gps;

    public LocationClass(Context context) {
        gps = new GpsTracker(context);
    }

    public double getLat () {
        double lat=0.0;
        if(gps.canGetLocation()){
            lat = gps.getLatitude();
            return lat;

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
            return 0.0;
        }
    }

    public double getLon () {
        double lon = 0.0;
        if(gps.canGetLocation()){
            lon = gps.getLongitude();
            return lon;

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            return 0.0;
        }
    }

}
