package com.example.pjaidmobile.di;

import android.util.Log;

import com.example.pjaidmobile.data.remote.api.DeviceApi;
import com.example.pjaidmobile.data.remote.api.ReportApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    private static final String TAG = "NetworkModule";
    private static final String BASE_URL = "https://a79c-2a02-a311-c0aa-c00-68ce-7d4c-ca17-628.ngrok-free.app/"; // Zmień

    @Provides
    @Singleton
    public Retrofit provideRetrofit() {
        Log.d(TAG, "Creating Retrofit instance with base URL: " + BASE_URL);
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public ReportApi provideReportApi(Retrofit retrofit) {
        Log.d(TAG, "Providing ReportApi instance");
        return retrofit.create(ReportApi.class);
    }

    @Provides
    @Singleton
    public DeviceApi provideDeviceApi(Retrofit retrofit) {
        Log.d(TAG, "Providing DeviceApi instance");
        return retrofit.create(DeviceApi.class);
    }

}
