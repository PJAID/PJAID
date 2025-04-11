package com.example.pjaidmobile.data.remote.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://a79c-2a02-a311-c0aa-c00-68ce-7d4c-ca17-628.ngrok-free.app/") // ← localhost z perspektywy emulatora!
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;

    }
}
