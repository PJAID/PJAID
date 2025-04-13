package com.example.pjaidmobile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TicketDetailActivity extends AppCompatActivity {
    private TextView tvTitle, tvDescription, tvStatus, tvAssignee, tvDate;
    private Button btnEdit, btnDelete;
    private String ticketId;

    // Klasa Ticket - minimalna implementacja potrzebna do wyświetlenia szczegółów
    public class Ticket {
        private String id;
        private String title;
        private String description;
        private String status;
        private String assignee;
        private long createdAt;

        public Ticket(String id, String title, String description, String status, String assignee) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.status = status;
            this.assignee = assignee;
            this.createdAt = System.currentTimeMillis();
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getStatus() { return status; }
        public String getAssignee() { return assignee; }

        public String getFormattedDate() {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault());
            return sdf.format(new Date(createdAt));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        // Initialize views
        tvTitle = findViewById(R.id.tv_detail_title);
        tvDescription = findViewById(R.id.tv_detail_description);
        tvStatus = findViewById(R.id.tv_detail_status);
        tvAssignee = findViewById(R.id.tv_detail_assignee);
        tvDate = findViewById(R.id.tv_detail_date);
        btnEdit = findViewById(R.id.btn_edit_ticket);
        btnDelete = findViewById(R.id.btn_delete_ticket);

        // Get ticket ID from intent
        ticketId = getIntent().getStringExtra("TICKET_ID");

        // Utworzenie przykładowego ticketu
        loadSampleTicket();

        // Obsługa przycisków - demonstracja)
        btnEdit.setOnClickListener(v ->
                Toast.makeText(this, "Edytuj clicked", Toast.LENGTH_SHORT).show());

        btnDelete.setOnClickListener(v ->
                Toast.makeText(this, "Usuń clicked", Toast.LENGTH_SHORT).show());
    }

    // Metoda demonstracyjna, normalnie pobieramy dane z TicketManager
    private void loadSampleTicket() {
        // Tworzenie przykładowego ticketu
        Ticket sampleTicket = new Ticket(
                "Ticket_001",
                "Szczegóły zgłoszenia",
                "Opis incidentu.",
                "np. Nowy",
                "np. Operator"
        );

        // Wypełnianie widoków danymi
        tvTitle.setText(sampleTicket.getTitle());
        tvDescription.setText(sampleTicket.getDescription());
        tvStatus.setText("Status: " + sampleTicket.getStatus());
        tvAssignee.setText("Assigned to: " + sampleTicket.getAssignee());
        tvDate.setText("Created: " + sampleTicket.getFormattedDate());
    }
}