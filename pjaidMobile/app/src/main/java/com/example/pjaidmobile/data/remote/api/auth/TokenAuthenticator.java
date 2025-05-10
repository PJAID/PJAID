package com.example.pjaidmobile.data.remote.api.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.example.pjaidmobile.data.model.AuthResponse;
import com.example.pjaidmobile.data.remote.api.AuthApi;
import com.example.pjaidmobile.presentation.features.auth.LoginActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Retrofit;

public class TokenAuthenticator implements Authenticator {

    private final Context context;
    private final Retrofit retrofit;

    public TokenAuthenticator(Context context, Retrofit retrofit) {
        this.context = context;
        this.retrofit = retrofit;
    }

    @Nullable
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        SharedPreferences prefs = context.getSharedPreferences("PJAIDPrefs", Context.MODE_PRIVATE);
        String refreshToken = prefs.getString("refreshToken", null);

        if (refreshToken == null) {
            clearAndLogout();
            return null;
        }

        Map<String, String> body = new HashMap<>();
        body.put("refreshToken", refreshToken);

        AuthApi authApi = retrofit.create(AuthApi.class);
        Call<AuthResponse> call = authApi.refresh(body);
        retrofit2.Response<AuthResponse> refreshResponse = call.execute();

        if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {
            String newAccessToken = refreshResponse.body().getAccessToken();

            prefs.edit().putString("accessToken", newAccessToken).apply();

            return response.request().newBuilder()
                    .header("Authorization", "Bearer " + newAccessToken)
                    .build();
        } else {
            clearAndLogout();
            return null;
        }
    }

    private void clearAndLogout() {
        SharedPreferences prefs = context.getSharedPreferences("PJAIDPrefs", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}