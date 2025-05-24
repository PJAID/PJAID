package com.example.pjaidmobile.util;

public interface LocationProvider {
    void getCurrentLocation(LocationCallback callback);

    interface LocationCallback {
        void onLocationReceived(double latitude, double longitude);

        void onLocationError(String message);
    }
}

