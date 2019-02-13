package com.rcacao.geolocation;

import android.Manifest;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class PlayServiceLocationViewModel extends AndroidViewModel implements OnSuccessListener<Location> {

    private MutableLiveData<Location> location = new MutableLiveData<>();
    private FusedLocationProviderClient locationClient;

    public PlayServiceLocationViewModel(@NonNull Application application) {
        super(application);
    }


    void startLocationRequest() {

        if (locationClient == null) {
            locationClient = LocationServices.getFusedLocationProviderClient(getApplication());
        }

        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationClient.getLastLocation().addOnSuccessListener(this);
        }

    }


    LiveData<Location> getLocation() {
        return location;
    }


    @Override
    public void onSuccess(Location receivedLocation) {
        location.setValue(receivedLocation);
    }

}
