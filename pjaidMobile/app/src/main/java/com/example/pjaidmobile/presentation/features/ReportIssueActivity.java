package com.example.pjaidmobile.presentation.features;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.data.model.IssueReport;
import com.example.pjaidmobile.data.remote.api.ReportApi;
import com.example.pjaidmobile.data.remote.service.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportIssueActivity extends AppCompatActivity {
    EditText editTextDescription;
    TextView textViewDeviceInfo;
    Button buttonSubmit;

    String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);

        editTextDescription = findViewById(R.id.editTextDescription);
        textViewDeviceInfo = findViewById(R.id.textViewDeviceInfo);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Odbierz dane z Intentu
        Intent intent = getIntent();
        deviceId = intent.getStringExtra("deviceId");
        String name = intent.getStringExtra("deviceName");
        String sn = intent.getStringExtra("serialNumber");
        String purchase = intent.getStringExtra("purchaseDate");
        String lastService = intent.getStringExtra("lastService");

        String info = "ID: " + deviceId + "\nNazwa: " + name + "\nS/N: " + sn + "\nZakup: " + purchase + "\nPrzegląd: " + lastService;
        textViewDeviceInfo.setText(info);

        buttonSubmit.setOnClickListener(v -> {
            String description = editTextDescription.getText().toString();
            if (!description.isEmpty()) {
                sendReport(new IssueReport(deviceId, description));
            } else {
                Toast.makeText(this, "Wpisz opis awarii", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendReport(IssueReport report) {
        ReportApi api = ApiClient.getClient().create(ReportApi.class);
        api.sendReport(report).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(ReportIssueActivity.this, "Zgłoszenie wysłane!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ReportIssueActivity.this, "Błąd zgłoszenia: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}