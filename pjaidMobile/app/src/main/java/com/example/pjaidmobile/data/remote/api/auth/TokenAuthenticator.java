package com.example.pjaidmobile.data.remote.api.auth;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.example.pjaidmobile.data.model.AuthResponse;
import com.example.pjaidmobile.data.remote.api.AuthApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;

public class TokenAuthenticator implements Authenticator {

    private final Context context;
    private final Retrofit retrofit;
    private final LogoutHandler logoutHandler;

    public TokenAuthenticator(Context context, Retrofit retrofit, LogoutHandler logoutHandler) {
        this.context = context;
        this.retrofit = retrofit;
        this.logoutHandler = logoutHandler;
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
        retrofit2.Response<AuthResponse> refreshResponse = authApi.refresh(body).execute();

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
        logoutHandler.logout(context);
    }
}