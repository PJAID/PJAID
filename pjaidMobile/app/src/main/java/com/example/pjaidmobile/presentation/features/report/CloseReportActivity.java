package com.example.pjaidmobile.presentation.features.report;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CloseReportActivity extends AppCompatActivity {

    private EditText inputSummary, inputTimeSpent, inputNotes;
    private int ticketId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_report);

        inputSummary = findViewById(R.id.inputSummary);
        inputTimeSpent = findViewById(R.id.inputTimeSpent);
        inputNotes = findViewById(R.id.inputNotes);

        Button btnSave = findViewById(R.id.btnSaveReport);
        Button btnExportCsv = findViewById(R.id.btnExportCsv);
        Button btnExportXls = findViewById(R.id.btnExportXls);

        ticketId = getIntent().getIntExtra("ticketId", -1);

        btnSave.setOnClickListener(v -> saveToServer());
        btnExportCsv.setOnClickListener(v -> exportToCSV());
        btnExportXls.setOnClickListener(v -> exportToXLS());
    }

    private void exportToCSV() {
        String summary = inputSummary.getText().toString().replace(",", ";");
        String time = inputTimeSpent.getText().toString();
        String notes = inputNotes.getText().toString().replace(",", ";");

        try {
            File file = new File(getExternalFilesDir(null), "raport_zgloszenia.csv");
            FileWriter writer = new FileWriter(file);
            writer.append("Zgłoszenie ID,Czas,Działania,Uwagi\n");
            writer.append(ticketId + "," + time + "," + summary + "," + notes + "\n");
            writer.flush();
            writer.close();

            Toast.makeText(this, "CSV zapisany: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Błąd przy zapisie CSV", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportToXLS() {
        Toast.makeText(this, "Eksport do XLS niezaimplementowany", Toast.LENGTH_SHORT).show();
    }

    private void saveToServer() {
        Toast.makeText(this, "Raport zapisany lokalnie (placeholder)", Toast.LENGTH_SHORT).show();
    }
}
