package com.example.pjaidmobile.presentation.features.report;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.data.model.EditTicketActivity;
import com.example.pjaidmobile.data.model.TicketResponse;
import com.example.pjaidmobile.data.remote.api.ApiClient;
import com.example.pjaidmobile.data.remote.api.TicketApi;
import com.example.pjaidmobile.util.ButtonAnimationUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TicketDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDescription, tvStatus, tvAssignee, tvDate;
    private Button btnEdit, btnDelete;
    private TicketResponse ticket;


    private TicketApi ticketApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        // powiązanie widoków
        tvTitle = findViewById(R.id.tv_detail_title);
        tvDescription = findViewById(R.id.tv_detail_description);
        tvStatus = findViewById(R.id.tv_detail_status);
        tvAssignee = findViewById(R.id.tv_detail_assignee);
        tvDate = findViewById(R.id.tv_detail_date);
        btnEdit = findViewById(R.id.btn_edit_ticket);
        btnDelete = findViewById(R.id.btn_delete_ticket);

        ButtonAnimationUtil.applySpringAnimation(btnEdit);
        ButtonAnimationUtil.applySpringAnimation(btnDelete);

        ticketApi = ApiClient.getClient().create(TicketApi.class);

        // wyświetlenie danych
        String reportIdStr = getIntent().getStringExtra("reportId");
        if (reportIdStr != null) {
            try {
                int reportIdInt = Integer.parseInt(reportIdStr);
                tvTitle.setText("Zgłoszenie #" + reportIdStr);
                loadTicketFromBackend(reportIdInt);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Niepoprawny identyfikator zgłoszenia", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Brak ID zgłoszenia", Toast.LENGTH_SHORT).show();
        }

        btnEdit.setOnClickListener(v -> {
            if (ticket != null) {
                Intent intent = new Intent(this, EditTicketActivity.class);
                intent.putExtra("ticketId", String.valueOf(ticket.getId()));
                intent.putExtra("title", ticket.getTitle());
                intent.putExtra("description", ticket.getDescription());
                intent.putExtra("status", ticket.getStatus());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Zgłoszenie jeszcze się ładuje...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loadTicketFromBackend(int id) {
        ticketApi.getTicket(id).enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ticket = response.body();
                    tvTitle.setText(ticket.getTitle());
                    tvDescription.setText("Opis zgłoszenia: " + ticket.getDescription());
                    tvStatus.setText("Status: " + ticket.getStatus());
                    tvAssignee.setText("Przypisany do: " + ticket.getTechnicianName());
                    tvDate.setText("Utworzono: " + ticket.getCreatedAt());
                    Log.d("TICKET", "Urządzenie: " + ticket.getDevice().getName());
                    Log.d("TICKET", "Użytkownik: " + ticket.getUser().getUserName());
                    if (ticket.getIncident() != null) {
                        Log.d("TICKET", "Incydent: " + ticket.getIncident().getTitle());
                    } else {
                        Log.w("TICKET", "Brak powiązanego incydentu w tym zgłoszeniu");
                    }
                } else {
                    Toast.makeText(TicketDetailActivity.this, "Nie udało się pobrać zgłoszenia", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                Toast.makeText(TicketDetailActivity.this, "Błąd połączenia: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}



