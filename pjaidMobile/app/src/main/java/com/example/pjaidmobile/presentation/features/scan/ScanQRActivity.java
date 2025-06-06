package com.example.pjaidmobile.presentation.features.scan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.data.model.Device;
import com.example.pjaidmobile.presentation.features.report.CreateTicketActivity;
import com.example.pjaidmobile.util.DeviceIntentHelper;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScanQRActivity extends AppCompatActivity {

    private static final String TAG = "ScanQRActivity";
    private ScanQRViewModel viewModel;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    String scannedId = result.getContents();
                    Log.d(TAG, "Zeskanowano kod: " + scannedId);
                    viewModel.fetchDevice(scannedId);
                } else {
                    Log.d(TAG, "Skanowanie anulowane");
                    viewModel.scanCancelled();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ScanQRViewModel.class);

        Log.d(TAG, "onCreate - uruchamiam skaner QR");
        ScanOptions options = new ScanOptions();
        options.setPrompt(getString(R.string.scan_prompt));
        options.setBeepEnabled(true);
        options.setOrientationLocked(false);
        barcodeLauncher.launch(options);
        viewModel.uiState.observe(this, this::handleUiState);
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
            Toast.makeText(this, R.string.device_not_found, Toast.LENGTH_SHORT).show(); // Zasoby!
            finish();

        } else if (state instanceof ScanUiState.Error) {
            String message = ((ScanUiState.Error) state).message;
            Log.e(TAG, "Błąd API: " + message);
            String errorMessage = message != null ? message : getString(R.string.unknown_api_error); // Zasoby!
            Toast.makeText(this, getString(R.string.api_error_prefix) + errorMessage, Toast.LENGTH_LONG).show(); // Zasoby!
            finish();

        } else if (state instanceof ScanUiState.ScanCancelled) {
            Log.d(TAG, "Obsługa stanu anulowania skanowania");
            Toast.makeText(this, R.string.scan_cancelled, Toast.LENGTH_SHORT).show(); // Zasoby!
            finish();
        }
    }
}
