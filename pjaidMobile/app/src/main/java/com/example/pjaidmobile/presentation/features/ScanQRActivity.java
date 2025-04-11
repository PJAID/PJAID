package com.example.pjaidmobile.presentation.features;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.data.model.Device;
import com.example.pjaidmobile.data.remote.api.DeviceApi;
import com.example.pjaidmobile.data.remote.service.ApiClient;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanQRActivity extends AppCompatActivity {

    private static final String TAG = "ScanQRActivity"; // tag do logcat

    private final androidx.activity.result.ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    String scannedId = result.getContents();
                    Log.d(TAG, "Zeskanowano kod: " + scannedId); // logujemy co zeskanowano
                    fetchDeviceFromApi(scannedId);
                } else {
                    Log.d(TAG, "Skanowanie anulowane");
                    Toast.makeText(this, "Anulowano", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate - uruchamiam skaner QR");
        ScanOptions options = new ScanOptions();
        options.setPrompt("Zeskanuj kod QR");
        options.setBeepEnabled(true);
        options.setOrientationLocked(false);
        barcodeLauncher.launch(options);
    }

    private void fetchDeviceFromApi(String id) {
        Log.d(TAG, "Wysyłam zapytanie do API z ID: " + id);
        DeviceApi api = ApiClient.getClient().create(DeviceApi.class);
        api.getDeviceById(id).enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                Log.d(TAG, "Odpowiedź z API, kod: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Device device = response.body();
                    Log.d(TAG, "Dane urządzenia: " + device.getName());

                    String info = "Nazwa: " + device.getId() + "\n"
                            + "S/N: " + device.getSerialNumber() + "\n"
                            + "Zakup: " + device.getPurchaseDate() + "\n"
                            + "Przegląd: " + device.getLastService();

//                    new AlertDialog.Builder(ScanQRActivity.this)
//                            .setTitle("Urządzenie: " + device.getId())
//                            .setMessage(info)
//                            .setPositiveButton("OK", (dialog, which) -> finish())
//                            .show();
                    Intent intent = new Intent(ScanQRActivity.this, ReportIssueActivity.class);
                    intent.putExtra("deviceId", device.getId());
                    intent.putExtra("deviceName", device.getName());
                    intent.putExtra("serialNumber", device.getSerialNumber());
                    intent.putExtra("purchaseDate", device.getPurchaseDate());
                    intent.putExtra("lastService", device.getLastService());
                    startActivity(intent);
                    finish();
                } else {
                    Log.w(TAG, "Nie znaleziono urządzenia lub brak danych");
                    Toast.makeText(ScanQRActivity.this, "Nie znaleziono urządzenia", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                Log.e(TAG, "Błąd połączenia z API: " + t.getMessage(), t);
                Toast.makeText(ScanQRActivity.this, "Błąd połączenia: " + t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
