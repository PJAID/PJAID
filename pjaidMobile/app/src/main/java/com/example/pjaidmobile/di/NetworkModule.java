package com.example.pjaidmobile.di;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.pjaidmobile.data.remote.api.AuthApi;
import com.example.pjaidmobile.data.remote.api.DeviceApi;
import com.example.pjaidmobile.data.remote.api.ReportApi;
import com.example.pjaidmobile.data.remote.api.TicketApi;
import com.example.pjaidmobile.data.remote.api.auth.TokenAuthenticator;
import com.example.pjaidmobile.presentation.features.auth.LoginActivity;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
    @Named("refresh")
    public Retrofit provideRefreshRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(@ApplicationContext Context context, @Named("refresh") Retrofit refreshRetrofit) {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder();

                    SharedPreferences prefs = context.getSharedPreferences("PJAIDPrefs", Context.MODE_PRIVATE);
                    String token = prefs.getString("accessToken", null);
                    if (token != null) {
                        builder.header("Authorization", "Bearer " + token);
                    }

                    return chain.proceed(builder.build());
                })
                .authenticator(new TokenAuthenticator(context, refreshRetrofit, ctx -> {
                    Intent intent = new Intent(ctx, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ctx.startActivity(intent);
                }))
                .build();
    }


    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
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

    @Provides
    @Singleton
    public AuthApi provideAuthApi(Retrofit retrofit) {
        return retrofit.create(AuthApi.class);
    }

}
