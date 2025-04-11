package com.example.pjaidmobile.presentation.common;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.presentation.features.ReportIssueActivity;
import com.example.pjaidmobile.presentation.features.ScanQRActivity;

public class MainActivity extends AppCompatActivity {

    ImageButton buttonReportIssue, buttonScanQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonReportIssue = findViewById(R.id.buttonReportIssue);
        buttonScanQR = findViewById(R.id.buttonScanQR);

        buttonReportIssue.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ReportIssueActivity.class));
        });

        buttonScanQR.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ScanQRActivity.class));
        });
    }

}