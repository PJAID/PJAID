// Plik: CreateTicketActivity.java
package com.example.pjaidmobile.presentation.features.ticket;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.data.model.TicketRequest;
import com.example.pjaidmobile.data.remote.api.TicketApi;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class CreateTicketActivity extends AppCompatActivity {

    @Inject
    TicketApi ticketApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);

        EditText title = findViewById(R.id.editTextTitle);
        EditText description = findViewById(R.id.editTextDescription);
        Button sendButton = findViewById(R.id.buttonSubmitTicket);

        sendButton.setOnClickListener(v -> {
            TicketRequest request = new TicketRequest(
                    title.getText().toString(),
                    description.getText().toString(),
                    "NOWE",
                    125, // deviceId (na razie ręcznie)
                    44   // userId (na razie ręcznie)
            );

            ticketApi.createTicket(request).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CreateTicketActivity.this, "Zgłoszenie dodane!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CreateTicketActivity.this, "Błąd serwera", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(CreateTicketActivity.this, "Niepowodzenie: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}