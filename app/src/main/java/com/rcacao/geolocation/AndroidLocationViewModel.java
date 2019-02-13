package com.rcacao.geolocation;

import android.Manifest;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

public class AndroidLocationViewModel extends AndroidViewModel implements LocationListener {

    private MutableLiveData<Location> location = new MutableLiveData<>();
    private LocationManager locationManager;

    public AndroidLocationViewModel(@NonNull Application application) {
        super(application);
    }


    void startLocationRequest(GPS_TYPE gpsType, int minTime, int minDistance) {

        if (locationManager == null) {
            locationManager = (LocationManager)
                    getApplication().getSystemService(Context.LOCATION_SERVICE);
        }

        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && (gpsType == GPS_TYPE.GPS || gpsType == GPS_TYPE.BOTH)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
        }
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && (gpsType == GPS_TYPE.NETWORK || gpsType == GPS_TYPE.BOTH)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);
        }

    }


    LiveData<Location> getLocation() {
        return location;
    }

    @Override
    public void onLocationChanged(Location receivedLocation) {
        location.setValue(receivedLocation);
        stopWatchLocation();
    }

    void stopWatchLocation() {
        locationManager.removeUpdates(this);
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

    public enum GPS_TYPE {
        GPS, NETWORK, BOTH
    }
}
