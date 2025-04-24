package com.example.pjaidmobile.presentation.features.auth;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.presentation.common.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button signInButton;
    private TextView forgotPasswordText;
    private ImageView passwordVisibilityIcon;
    private boolean passwordVisible = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Sprawdzenie, czy użytkownik jest zalogowany
        SharedPreferences prefs = getSharedPreferences("PJAIDPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Jeśli jest zalogowany, przejdź do głównego menu
            startActivity(new Intent(this, MainActivity.class));
            finish(); // Zakończ aktywność logowania
            return;
        }

        // Inicjalizacja elementów interfejsu
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        rememberMeCheckBox = findViewById(R.id.remember_me_checkbox);
        signInButton = findViewById(R.id.sign_in_button);
        forgotPasswordText = findViewById(R.id.forgot_password_text);
        passwordVisibilityIcon = findViewById(R.id.password_visibility_icon);


        // tło
        ConstraintLayout mainLayout = findViewById(R.id.login_layout);
        mainLayout.setBackgroundResource(R.drawable.background);

        // logo
        ImageView logoImageView = findViewById(R.id.logo_image);
        logoImageView.setImageResource(R.drawable.logo);

        //  zapisana nazwa użytkownika (jeśli była zaznaczona opcja "Zapamiętaj mnie")
        String savedUsername = prefs.getString("username", "");
        if (!savedUsername.isEmpty()) {
            usernameEditText.setText(savedUsername);
            rememberMeCheckBox.setChecked(true);
        }

        // obsługa przycisku pokazywania/ukrywania hasła
        passwordVisibilityIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        // Obsługa przycisku logowania
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        // Obsługa linku "Zapomniałem hasła"
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementacja funkcji resetowania hasła
                Toast.makeText(LoginActivity.this, "Funkcja resetowania hasła", Toast.LENGTH_SHORT).show();
                // Tu można dodać kod do otwierania ekranu resetowania hasła
                 startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    // Metoda do przełączania widoczności hasła
    private void togglePasswordVisibility() {
        if (passwordVisible) {
            // Ukryj hasło
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordVisibilityIcon.setImageResource(R.drawable.ic_visibility_off);
        } else {
            // Pokaż hasło
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordVisibilityIcon.setImageResource(R.drawable.ic_visibility);
        }
        passwordVisible = !passwordVisible;
        // Ustaw kursor na końcu tekstu
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    // Metoda do obsługi logowania
    private void attemptLogin() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean rememberMe = rememberMeCheckBox.isChecked();

        // Sprawdź, czy pola nie są puste
        if (username.isEmpty()) {
            usernameEditText.setError("Nazwa użytkownika nie może być pusta");
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Hasło nie może być puste");
            return;
        }

        // Tutaj dodaj właściwą logikę weryfikacji danych logowania
        // Na potrzeby przykładu zakładamy, że logowanie zawsze się udaje

        // Wyświetl informację o próbie logowania
        Toast.makeText(this, "Logowanie: " + username, Toast.LENGTH_SHORT).show();

        // Zapisz stan logowania
        SharedPreferences prefs = getSharedPreferences("PJAIDPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", true);

        if (rememberMe) {
            editor.putString("username", username);
        } else {
            editor.remove("username");
        }

        editor.apply();

        // Przekieruj do menu głównego
        startActivity(new Intent(this, MainActivity.class));
        finish(); // Zakończ aktywność logowania
    }
}
