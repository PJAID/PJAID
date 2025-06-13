package com.example.pjaidmobile.data.model;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.presentation.features.report.CloseReportActivity;

public class EditTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ticket);

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        Spinner spinnerStatus = findViewById(R.id.spinner_status);
        Button btnSaveChanges = findViewById(R.id.button_save_changes);

        String ticketId = getIntent().getStringExtra("ticketId");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.status_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        btnSaveChanges.setOnClickListener(v -> {
            String selectedStatus = spinnerStatus.getSelectedItem().toString().toLowerCase();

            Toast.makeText(this, "Zapisano zmiany dla zgłoszenia: " + ticketId, Toast.LENGTH_SHORT).show();

            if (selectedStatus.contains("zamknięte") || selectedStatus.contains("zakończone")) {
                Intent intent = new Intent(this, CloseReportActivity.class);
                intent.putExtra("ticketId", ticketId);
                startActivity(intent);
            } else {
                finish();
            }
        });
    }
}
