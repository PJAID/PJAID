package com.example.pjaidmobile.presentation.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.presentation.features.auth.LoginActivity;
import com.example.pjaidmobile.presentation.features.report.ReportListActivity;
import com.example.pjaidmobile.presentation.features.scan.ScanQRActivity;
import com.example.pjaidmobile.presentation.features.ticket.CreateTicketActivity;
import com.example.pjaidmobile.presentation.features.ticket.TicketDetailActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sprawdź, czy użytkownik jest zalogowany
        SharedPreferences prefs = getSharedPreferences("PJAIDPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // Jeśli nie jest zalogowany, przekieruj na ekran logowania
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // Zakończ tę aktywność, żeby użytkownik nie mógł wrócić za pomocą przycisku wstecz
            return;
        }

        // Kontynuuj normalne uruchamianie menu głównego
        setContentView(R.layout.activity_main);

        Button scanQR = findViewById(R.id.buttonScanQR);
        Button reportIssue = findViewById(R.id.buttonReportIssue);
        Button reportList = findViewById(R.id.buttonReportList);
        Button reportDetail = findViewById(R.id.buttonReportDetail);
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        scanQR.setOnClickListener(v ->
                startActivity(new Intent(this, ScanQRActivity.class)));

        reportIssue.setOnClickListener(v ->
                startActivity(new Intent(this, CreateTicketActivity.class)));

        reportList.setOnClickListener(v ->
                startActivity(new Intent(this, ReportListActivity.class)));

        reportDetail.setOnClickListener(v -> {
            Intent intent = new Intent(this, TicketDetailActivity.class);
            intent.putExtra("TICKET_ID", "TK001"); //  przykładowe ID
            startActivity(intent);
        });
    }
}