package com.comp3617.finalproject;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Andrew on 2017-07-07.
 */

public class LocationServices implements LocationListener {
    static String LOGTAG = "Andrew";

    public LocationListener getLocationListener(){
        return this;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOGTAG,location.getLongitude()+" "+ location.getLatitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
