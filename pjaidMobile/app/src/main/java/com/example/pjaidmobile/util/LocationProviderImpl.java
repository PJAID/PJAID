package com.example.pjaidmobile.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class LocationProviderImpl implements LocationProvider {
    private final Context context;

    @Inject
    public LocationProviderImpl(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public void getCurrentLocation(LocationCallback callback) {
        // Sprawdzenie uprawnień
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            callback.onLocationError("Brak uprawnień do lokalizacji");
            return;
        }

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);

        @SuppressLint("MissingPermission")
        var task = client.getLastLocation();

        task.addOnSuccessListener(location -> {
            if (location != null) {
                callback.onLocationReceived(location.getLatitude(), location.getLongitude());
            } else {
                callback.onLocationError("Lokalizacja niedostępna");
            }
        }).addOnFailureListener(e -> callback.onLocationError("Błąd pobierania lokalizacji: " + e.getMessage()));
    }
}