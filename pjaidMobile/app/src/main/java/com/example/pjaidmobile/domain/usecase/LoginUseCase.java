package com.example.pjaidmobile.domain.usecase;

import com.example.pjaidmobile.data.model.AuthResponse;
import com.example.pjaidmobile.domain.repository.AuthRepository;

import javax.inject.Inject;

import retrofit2.Call;

public class LoginUseCase {
    private final AuthRepository repository;

    @Inject
    public LoginUseCase(AuthRepository repository) {
        this.repository = repository;
    }

    public Call<AuthResponse> execute(String username, String password) {
        return repository.login(username, password);
    }
}
