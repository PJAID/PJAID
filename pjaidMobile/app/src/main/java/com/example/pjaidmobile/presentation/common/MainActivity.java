package com.example.pjaidmobile.presentation.common;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.presentation.features.report.TicketNotificationService;
import com.example.pjaidmobile.util.ButtonAnimationUtil;
import com.example.pjaidmobile.presentation.features.auth.LoginActivity;
import com.example.pjaidmobile.presentation.features.report.ReportListActivity;
import com.example.pjaidmobile.presentation.features.report.TicketDetailActivity;
import com.example.pjaidmobile.presentation.features.scan.ScanQRActivity;
import com.example.pjaidmobile.presentation.features.ticket.CreateTicketActivity;

import javax.inject.Inject;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;

    @Inject
    TicketNotificationService ticketNotificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sprawdzenie logowania
        SharedPreferences prefs = getSharedPreferences("PJAIDPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Utworzenie kanału powiadomień (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id",
                    "Powiadomienia PJAID",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        // Prośba o uprawnienia do powiadomień (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }

        // Przyciski
        Button scanQR = findViewById(R.id.buttonScanQR);
        Button reportIssue = findViewById(R.id.buttonReportIssue);
        Button reportList = findViewById(R.id.buttonReportList);
        Button reportDetail = findViewById(R.id.buttonReportDetail);
        Button logoutButton = findViewById(R.id.logout_button);

        // Powiązanie zalogowanego użytkownika
        TextView loggedUserText = findViewById(R.id.tv_logged_user);
        String loggedUser = prefs.getString("username", "Nieznany użytkownik");
        loggedUserText.setText("Zalogowano jako: " + loggedUser);

        // Animacja przycisków
        ButtonAnimationUtil.applySpringAnimation(scanQR);
        ButtonAnimationUtil.applySpringAnimation(reportIssue);
        ButtonAnimationUtil.applySpringAnimation(reportList);
        ButtonAnimationUtil.applySpringAnimation(reportDetail);
        ButtonAnimationUtil.applySpringAnimation(logoutButton);

        // Obsługa kliknięć
        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        scanQR.setOnClickListener(v -> startActivity(new Intent(this, ScanQRActivity.class)));

        reportIssue.setOnClickListener(v -> startActivity(new Intent(this, CreateTicketActivity.class)));

        reportList.setOnClickListener(v -> startActivity(new Intent(this, ReportListActivity.class)));

        reportDetail.setOnClickListener(v -> {
            Intent intent = new Intent(this, TicketDetailActivity.class);
            intent.putExtra("TICKET_ID", "TK001");
            startActivity(intent);
        });
        Button testNotification = findViewById(R.id.buttonTestNotification);
        ButtonAnimationUtil.applySpringAnimation(testNotification);

        testNotification.setOnClickListener(v -> {
            ticketNotificationService.showStatusChanged("testowe powiadomienie");
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Zgoda udzielona
            } else {
                // Użytkownik odmówił
            }
        }
    }

}
