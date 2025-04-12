package com.example.pjaidmobile;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

/**
 * Wymaganym przez Hilt klasa będąca punktem startowym, który umożliwia
 * mu zainicjalizowanie mechanizmu wstrzykiwania zależności dla całej aplikacji.
 */
@HiltAndroidApp
public class App extends Application {
}
