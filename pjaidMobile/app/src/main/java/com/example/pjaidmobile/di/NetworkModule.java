package com.example.pjaidmobile.di;

import android.util.Log;

import com.example.pjaidmobile.data.remote.api.DeviceApi;
import com.example.pjaidmobile.data.remote.api.ReportApi;
import com.example.pjaidmobile.data.remote.api.TicketApi;

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
    private static final String BASE_URL = "http://10.0.2.2:8080/";

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

    @Provides
    @Singleton
    public TicketApi provideTicketApi(Retrofit retrofit) {
        return retrofit.create(TicketApi.class);
    }

}
