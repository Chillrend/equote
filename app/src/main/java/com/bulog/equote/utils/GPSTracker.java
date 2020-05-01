package com.bulog.equote.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class GPSTracker {
    Timer timer;
    LocationManager locationManager;
    LocationResult locationResult;

    boolean gpsEnabled = false;
    boolean networkEnabled = false;


    @SuppressLint("MissingPermission")
    public boolean getLocation(Context context, LocationResult result){
        locationResult = result;

        if(locationManager == null){
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception e){
            Log.e("OnGPSTracker", e.getMessage());
        }

        try {
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception e){
            Log.e("OnGPSTracker", e.getMessage());
        }

        if(!gpsEnabled && !networkEnabled) return false;

        if(gpsEnabled){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
        }else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);
        }

        timer = new Timer();
        timer.schedule(new GetLastLocation(), 20000);
        return true;
    }

    LocationListener gpsListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            timer.cancel();
            locationResult.gotLocation(location);
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(networkListener);
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
    };
    LocationListener networkListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            timer.cancel();
            locationResult.gotLocation(location);
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(gpsListener);
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
    };

    class GetLastLocation extends TimerTask{
        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            locationManager.removeUpdates(gpsListener);
            locationManager.removeUpdates(networkListener);

            Location netLoc = null, gpsLoc = null;

            if(gpsEnabled)
                gpsLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            else if(networkEnabled)
                netLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(gpsLoc != null && netLoc != null){
                if(gpsLoc.getTime() > netLoc.getTime())
                    locationResult.gotLocation(gpsLoc);
                else
                    locationResult.gotLocation(netLoc);
            }

            if(gpsLoc != null){
                locationResult.gotLocation(gpsLoc);
                return;
            }

            if(netLoc != null){
                locationResult.gotLocation(netLoc);
                return;
            }

            locationResult.gotLocation(null);
        }
    }

    public static abstract class LocationResult{
        public abstract void  gotLocation(Location location);
    }
}
