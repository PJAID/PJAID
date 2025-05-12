package com.example.pjaidmobile.domain.repository;

import com.example.pjaidmobile.data.model.AuthResponse;

import retrofit2.Call;

public interface AuthRepository {
    Call<AuthResponse> login(String username, String password);
}
