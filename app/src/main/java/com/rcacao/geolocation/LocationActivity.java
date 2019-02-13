package com.rcacao.geolocation;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class LocationActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 300;
    private AndroidLocationViewModel viewModelAndroid;
    private PlayServiceLocationViewModel viewModelPlayService;
    private TextView textViewLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        textViewLocation = findViewById(R.id.textViewLocation);

        viewModelAndroid = ViewModelProviders.of(this).get(AndroidLocationViewModel.class);
        viewModelPlayService = ViewModelProviders.of(this).get(PlayServiceLocationViewModel.class);

        subscribe();

        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};

        if (!hasPermission(permissions)) {
            requestNecessaryPermissions(permissions);
        }

    }

    private void subscribe() {
        viewModelAndroid.getLocation().observe(this, location -> showLocationInfo("Android Location", location));
        viewModelPlayService.getLocation().observe(this, location -> showLocationInfo("Play Service", location));

    }

    private boolean hasPermission(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestNecessaryPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
    }


    private void showLocationInfo(String origin, Location location) {

        String text = origin + "\n\n";

        if (location != null) {
            text += "latitude: " + location.getLatitude() + "\n" +
                    "longitude: " + location.getLongitude() + "\n" +
                    "accuracy: " + location.getAccuracy();
        } else {
            text += "Não encontrado!";
        }
        textViewLocation.setText(text);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModelAndroid.stopWatchLocation();
    }

    public void onClickAndroidLocation(View view) {
        viewModelAndroid.startLocationRequest(AndroidLocationViewModel.GPS_TYPE.BOTH, 0, 10);
    }

    public void onClickPlayServiceLocation(View view) {
        viewModelPlayService.startLocationRequest();
    }
}
