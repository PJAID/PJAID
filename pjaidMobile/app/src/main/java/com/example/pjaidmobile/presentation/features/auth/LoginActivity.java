package com.example.pjaidmobile.presentation.features.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.data.model.AuthResponse;
import com.example.pjaidmobile.domain.usecase.LoginUseCase;
import com.example.pjaidmobile.presentation.common.MainActivity;
import com.example.pjaidmobile.util.ButtonAnimationUtil;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    @Inject
    LoginUseCase loginUseCase;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button signInButton;
    private TextView forgotPasswordText;
    private TextView signupText;
    private TextView loginErrorText;

    private ImageView passwordVisibilityIcon;
    private boolean passwordVisible = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView logoImageView = findViewById(R.id.logo_image);
        logoImageView.setImageResource(R.drawable.logo);

        // animacja
        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_appear);
        logoImageView.startAnimation(logoAnimation);


        // sprawdzenie logowania
        SharedPreferences prefs = getSharedPreferences("PJAIDPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // inicjalizacja elementów
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        rememberMeCheckBox = findViewById(R.id.remember_me_checkbox);
        loginErrorText = findViewById(R.id.login_error_text);
        signInButton = findViewById(R.id.sign_in_button);
        forgotPasswordText = findViewById(R.id.forgot_password_text);
        signupText = findViewById(R.id.signup_text);
        passwordVisibilityIcon = findViewById(R.id.password_visibility_icon);

        // animacje kliknięcia
        ButtonAnimationUtil.applySpringAnimation(signInButton);
        ButtonAnimationUtil.applySpringAnimation(signupText);
        ButtonAnimationUtil.applySpringAnimation(forgotPasswordText);

        // kolorowy napis "Sign Up"
        String fullText = "Don't have an account? Sign Up";
        SpannableString spannableString = new SpannableString(fullText);
        ForegroundColorSpan blueColor = new ForegroundColorSpan(Color.parseColor("#00BCD4"));
        spannableString.setSpan(blueColor, 23, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupText.setText(spannableString);

        // obsługa kliknięć
        signupText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        forgotPasswordText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
        });

        signInButton.setOnClickListener(v -> {
            attemptLogin();
        });

        passwordVisibilityIcon.setOnClickListener(v -> togglePasswordVisibility());

        // obsługa zapamiętania użytkownika
        String savedUsername = prefs.getString("username", "");
        if (!savedUsername.isEmpty()) {
            usernameEditText.setText(savedUsername);
            rememberMeCheckBox.setChecked(true);
        }
    }

    private void togglePasswordVisibility() {
        if (passwordVisible) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordVisibilityIcon.setImageResource(R.drawable.ic_visibility_off);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordVisibilityIcon.setImageResource(R.drawable.ic_visibility);
        }
        passwordVisible = !passwordVisible;
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    private void attemptLogin() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean rememberMe = rememberMeCheckBox.isChecked();

        if (username.isEmpty()) {
            usernameEditText.setError("Nazwa użytkownika nie może być pusta");
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Hasło nie może być puste");
            return;
        }

        loginUseCase.execute(username, password).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String accessToken = response.body().getAccessToken();
                    String refreshToken = response.body().getRefreshToken();

                    SharedPreferences prefs = getSharedPreferences("PJAIDPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("accessToken", accessToken);
                    editor.putString("refreshToken", refreshToken);

                    if (rememberMe) {
                        editor.putString("username", username);
                    } else {
                        editor.remove("username");
                    }

                    editor.apply();

                    loginErrorText.setVisibility(View.GONE);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    loginErrorText.setText("Nieprawidłowy login lub hasło");
                    loginErrorText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                loginErrorText.setText("Błąd połączenia z serwerem");
                loginErrorText.setVisibility(View.VISIBLE);
            }
        });
    }

}
