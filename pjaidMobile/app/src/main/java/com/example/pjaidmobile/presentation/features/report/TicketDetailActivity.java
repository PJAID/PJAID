package com.example.pjaidmobile.presentation.features.report;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.data.model.TicketResponse;
import com.example.pjaidmobile.data.remote.api.ApiClient;
import com.example.pjaidmobile.data.remote.api.TicketApi;
import com.example.pjaidmobile.util.ButtonAnimationUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketDetailActivity extends AppCompatActivity {

    private static final String STATUS_ZAKONCZONE = "ZAKOŃCZONE";

    private TicketResponse ticket;
    private TicketApi ticketApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        TextView tvTitle = findViewById(R.id.tv_detail_title);
        TextView tvDescription = findViewById(R.id.tv_detail_description);
        TextView tvStatus = findViewById(R.id.tv_detail_status);
        TextView tvAssignee = findViewById(R.id.tv_detail_assignee);
        TextView tvDate = findViewById(R.id.tv_detail_date);

        EditText etDescription = findViewById(R.id.et_description);
        EditText etReportDesc = findViewById(R.id.et_report_description);
        EditText etReportDuration = findViewById(R.id.et_report_duration);
        EditText etReportNotes = findViewById(R.id.et_report_notes);

        Spinner spinnerStatus = findViewById(R.id.spinner_status);
        Spinner spinnerAssignee = findViewById(R.id.spinner_assignee);
        LinearLayout reportSection = findViewById(R.id.report_section);

        Button btnEdit = findViewById(R.id.btn_edit_ticket);
        Button btnSaveChanges = findViewById(R.id.btn_save_changes);

        ButtonAnimationUtil.applySpringAnimation(btnEdit);

        ticketApi = ApiClient.getClient().create(TicketApi.class);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"NOWE", "W TRAKCIE", STATUS_ZAKONCZONE});
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        ArrayAdapter<String> assigneeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"-- Wybierz --", "Technik 1", "Technik 2"});
        assigneeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssignee.setAdapter(assigneeAdapter);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = parent.getItemAtPosition(position).toString();
                reportSection.setVisibility(STATUS_ZAKONCZONE.equals(selectedStatus) ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // intentionally left blank
            }
        });

        btnEdit.setOnClickListener(v -> {
            if (ticket != null) {
                tvDescription.setVisibility(View.GONE);
                tvStatus.setVisibility(View.GONE);
                tvAssignee.setVisibility(View.GONE);

                etDescription.setVisibility(View.VISIBLE);
                spinnerStatus.setVisibility(View.VISIBLE);
                spinnerAssignee.setVisibility(View.VISIBLE);

                btnEdit.setVisibility(View.GONE);
                btnSaveChanges.setVisibility(View.VISIBLE);

                etDescription.setText(ticket.getDescription());
                switch (ticket.getStatus()) {
                    case "NOWE": spinnerStatus.setSelection(0); break;
                    case "W TRAKCIE": spinnerStatus.setSelection(1); break;
                    case STATUS_ZAKONCZONE: spinnerStatus.setSelection(2); break;
                    default: spinnerStatus.setSelection(0); break;
                }

                if (STATUS_ZAKONCZONE.equals(ticket.getStatus())) {
                    reportSection.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "Zgłoszenie jeszcze się ładuje...", Toast.LENGTH_SHORT).show();
            }
        });

        btnSaveChanges.setOnClickListener(v -> {
            if (ticket == null) return;

            ticket.setDescription(etDescription.getText().toString());
            ticket.setStatus(spinnerStatus.getSelectedItem().toString());
            ticket.setTechnicianName(spinnerAssignee.getSelectedItem().toString());

            if (STATUS_ZAKONCZONE.equals(ticket.getStatus())) {
                ticket.setReportDescription(etReportDesc.getText().toString());
                ticket.setReportDuration(etReportDuration.getText().toString());
                ticket.setReportNotes(etReportNotes.getText().toString());
            }

            ticketApi.updateTicket(ticket.getId(), ticket).enqueue(new Callback<TicketResponse>() {
                @Override
                public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(TicketDetailActivity.this, "Zapisano zmiany", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(TicketDetailActivity.this, "Błąd zapisu", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TicketResponse> call, Throwable t) {
                    Toast.makeText(TicketDetailActivity.this, "Błąd: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        String reportIdStr = getIntent().getStringExtra("reportId");
        if (reportIdStr != null) {
            try {
                int reportIdInt = Integer.parseInt(reportIdStr);
                tvTitle.setText("Zgłoszenie #" + reportIdStr);
                loadTicketFromBackend(reportIdInt, tvTitle, tvDescription, tvStatus, tvAssignee, tvDate);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Niepoprawny identyfikator zgłoszenia", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Brak ID zgłoszenia", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadTicketFromBackend(int id, TextView tvTitle, TextView tvDescription, TextView tvStatus, TextView tvAssignee, TextView tvDate) {
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
