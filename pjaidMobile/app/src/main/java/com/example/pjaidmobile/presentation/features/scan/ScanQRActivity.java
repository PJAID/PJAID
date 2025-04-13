package com.example.pjaidmobile.presentation.features.scan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.data.model.Device;
import com.example.pjaidmobile.presentation.features.report.ReportIssueActivity;
import com.example.pjaidmobile.util.Constants;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScanQRActivity extends AppCompatActivity {

    private static final String TAG = "ScanQRActivity"; // tag do logcat
    private ScanQRViewModel viewModel; // Pole ViewModel

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    String scannedId = result.getContents();
                    Log.d(TAG, "Zeskanowano kod: " + scannedId);
                    // Deleguj do ViewModel
                    viewModel.fetchDevice(scannedId);
                } else {
                    Log.d(TAG, "Skanowanie anulowane");
                    // Deleguj do ViewModel lub bezpośrednio obsłuż stan UI
                    viewModel.scanCancelled();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicjalizacja ViewModelu
        viewModel = new ViewModelProvider(this).get(ScanQRViewModel.class);

        Log.d(TAG, "onCreate - uruchamiam skaner QR");
        ScanOptions options = new ScanOptions();
        options.setPrompt(getString(R.string.scan_prompt)); // Użyj zasobów!
        options.setBeepEnabled(true);
        options.setOrientationLocked(false);
        barcodeLauncher.launch(options);

        // Obserwuj zmiany stanu UI
        viewModel.uiState.observe(this, this::handleUiState);
    }

    private void handleUiState(ScanUiState state) {
        // Tutaj możesz pokazać/ukryć ProgressBar, jeśli jest
        // np. progressBar.setVisibility(state instanceof ScanUiState.FetchingDevice ? View.VISIBLE : View.GONE);

        if (state instanceof ScanUiState.DeviceFound) {
            Device device = ((ScanUiState.DeviceFound) state).device; // Lub DomainDevice
            Log.d(TAG, "Znaleziono urządzenie: " + device.getName());
            Intent intent = getIntent(device);
            startActivity(intent);
            finish(); // Zamknij po uruchomieniu następnej aktywności

        } else if (state instanceof ScanUiState.DeviceNotFound) {
            Log.w(TAG, "Nie znaleziono urządzenia w API");
            Toast.makeText(this, R.string.device_not_found, Toast.LENGTH_SHORT).show(); // Zasoby!
            finish(); // Zamknij po błędzie

        } else if (state instanceof ScanUiState.Error) {
            String message = ((ScanUiState.Error) state).message;
            Log.e(TAG, "Błąd API: " + message);
            String errorMessage = message != null ? message : getString(R.string.unknown_api_error); // Zasoby!
            Toast.makeText(this, getString(R.string.api_error_prefix) + errorMessage, Toast.LENGTH_LONG).show(); // Zasoby!
            finish(); // Zamknij po błędzie

        } else if (state instanceof ScanUiState.ScanCancelled) {
            Log.d(TAG, "Obsługa stanu anulowania skanowania");
            Toast.makeText(this, R.string.scan_cancelled, Toast.LENGTH_SHORT).show(); // Zasoby!
            finish(); // Zamknij po anulowaniu
        }
        // Można dodać obsługę Idle, Scanning, FetchingDevice (np. pokazanie ProgressDialogu)
    }

    @NonNull
    private Intent getIntent(Device device) {
        Intent intent = new Intent(ScanQRActivity.this, ReportIssueActivity.class);
        intent.putExtra(Constants.KEY_DEVICE_ID, device.getId());
        intent.putExtra(Constants.KEY_DEVICE_NAME, device.getName());
        intent.putExtra(Constants.KEY_SERIAL_NUMBER, device.getSerialNumber());
        intent.putExtra(Constants.KEY_PURCHASE_DATE, device.getPurchaseDate());
        intent.putExtra(Constants.KEY_LAST_SERVICE, device.getLastService());
        return intent;
    }
}
