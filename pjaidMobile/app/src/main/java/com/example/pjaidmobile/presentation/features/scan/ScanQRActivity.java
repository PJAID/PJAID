package com.example.pjaidmobile.presentation.features.scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.data.model.Device;
import com.example.pjaidmobile.presentation.features.report.CreateTicketActivity;
import com.example.pjaidmobile.util.DeviceIntentHelper;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScanQRActivity extends AppCompatActivity {

    private static final String TAG = "ScanQRActivity";
    private static final int CAMERA_PERMISSION_REQUEST = 100;

    private ScanQRViewModel viewModel;
    private DecoratedBarcodeView barcodeScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        viewModel = new ViewModelProvider(this).get(ScanQRViewModel.class);
        barcodeScannerView = findViewById(R.id.barcodeScannerView);

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> finish());
        }

        viewModel.uiState.observe(this, this::handleUiState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            startScanning();
        }
    }

    private void startScanning() {
        barcodeScannerView.decodeContinuous(result -> {
            if (result != null && result.getText() != null) {
                String scannedId = result.getText();
                Log.d(TAG, "Zeskanowano kod: " + scannedId);
                barcodeScannerView.pause();
                viewModel.fetchDevice(scannedId);
            }
        });
        barcodeScannerView.resume();
    }

    private void handleUiState(ScanUiState state) {
        if (state instanceof ScanUiState.DeviceFound) {
            Device device = ((ScanUiState.DeviceFound) state).device;
            Log.d(TAG, "Znaleziono urządzenie: " + device.getName());

            Intent intent = DeviceIntentHelper.createIntentWithDevice(this, CreateTicketActivity.class, device);
            startActivity(intent);
            finish();

        } else if (state instanceof ScanUiState.DeviceNotFound) {
            Log.w(TAG, "Nie znaleziono urządzenia w API");
            Toast.makeText(this, R.string.device_not_found, Toast.LENGTH_SHORT).show();
            finish();

        } else if (state instanceof ScanUiState.Error) {
            String message = ((ScanUiState.Error) state).message;
            Log.e(TAG, "Błąd API: " + message);
            String errorMessage = message != null ? message : getString(R.string.unknown_api_error);
            Toast.makeText(this, getString(R.string.api_error_prefix) + errorMessage, Toast.LENGTH_LONG).show();
            finish();

        } else if (state instanceof ScanUiState.ScanCancelled) {
            Log.d(TAG, "Obsługa stanu anulowania skanowania");
            Toast.makeText(this, R.string.scan_cancelled, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            barcodeScannerView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning();
            } else {
                Toast.makeText(this, "Brak uprawnień do kamery", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
