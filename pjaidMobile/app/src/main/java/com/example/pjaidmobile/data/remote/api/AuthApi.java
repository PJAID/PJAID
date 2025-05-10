package com.example.pjaidmobile.data.remote.api;

import com.example.pjaidmobile.data.model.AuthRequest;
import com.example.pjaidmobile.data.model.AuthResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("/api/auth/refresh")
    Call<AuthResponse> refresh(@Body Map<String, String> body);

}