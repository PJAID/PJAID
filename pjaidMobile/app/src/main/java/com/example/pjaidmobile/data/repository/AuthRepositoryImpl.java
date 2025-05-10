package com.example.pjaidmobile.data.repository;

import com.example.pjaidmobile.data.model.AuthRequest;
import com.example.pjaidmobile.data.model.AuthResponse;
import com.example.pjaidmobile.data.remote.api.AuthApi;
import com.example.pjaidmobile.domain.repository.AuthRepository;

import javax.inject.Inject;
import retrofit2.Call;

public class AuthRepositoryImpl implements AuthRepository {
    private final AuthApi authApi;

    @Inject
    public AuthRepositoryImpl(AuthApi authApi) {
        this.authApi = authApi;
    }

    @Override
    public Call<AuthResponse> login(String username, String password) {
        return authApi.login(new AuthRequest(username, password));
    }
}
