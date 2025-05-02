package com.example.pjaidmobile.presentation.features.auth;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.util.ButtonAnimationUtil;

public class ResetPasswordActivity extends AppCompatActivity {

    private Button resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        // animacja sprężynowa
        ButtonAnimationUtil.applySpringAnimation(resetPasswordButton);

        resetPasswordButton.setOnClickListener(v -> {
            // obsługa resetowania hasła
        });
    }
}
