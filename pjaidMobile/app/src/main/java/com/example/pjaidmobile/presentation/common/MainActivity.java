package com.example.pjaidmobile.presentation.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.databinding.ActivityMainBinding;
import com.example.pjaidmobile.presentation.features.report.ReportIssueActivity;
import com.example.pjaidmobile.presentation.features.scan.ScanQRActivity;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Activity started");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d(TAG, "View binding initialized");

        binding.buttonReportIssue.setOnClickListener(v -> {
            Log.d(TAG, "Report Issue button clicked");
            startActivity(new Intent(MainActivity.this, ReportIssueActivity.class));
        });

        binding.buttonScanQR.setOnClickListener(v -> {
            Log.d(TAG, "Scan QR button clicked");
            startActivity(new Intent(MainActivity.this, ScanQRActivity.class));
        });
    }
}