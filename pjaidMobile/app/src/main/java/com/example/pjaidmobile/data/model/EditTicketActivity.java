package com.example.pjaidmobile.data.model;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.R;

public class EditTicketActivity extends AppCompatActivity {

    private EditText editTitle, editDescription;
    private Button btnSaveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ticket);

        editTitle = findViewById(R.id.edit_ticket_title);
        editDescription = findViewById(R.id.edit_ticket_description);
        btnSaveChanges = findViewById(R.id.button_save_changes);

        // odbiór przekazanego ID zgłoszenia
        String ticketId = getIntent().getStringExtra("TICKET_ID");

        // pobieranie danych zgłoszenia na podstawie ticketId (np. z repozytorium)
        // Na razie wersja demo

        btnSaveChanges.setOnClickListener(v -> {
            String newTitle = editTitle.getText().toString();
            String newDescription = editDescription.getText().toString();

            // zapisywanie zmian (np. wysyłasz na serwer, zapisujesz w bazie)
            Toast.makeText(this, "Zapisano zmiany dla zgłoszenia: " + ticketId, Toast.LENGTH_SHORT).show();

            // zamknięcie aktywności
            finish();
        });
    }
}
