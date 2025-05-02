package com.example.pjaidmobile.presentation.features.report;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.data.model.Ticket;
import com.example.pjaidmobile.util.ButtonAnimationUtil;

public class TicketDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDescription, tvStatus, tvAssignee, tvDate;
    private Button btnEdit, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

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


        // tymczasowy ticket demo
        Ticket sampleTicket = new Ticket(
                "TK001",
                "Tytuł zgłoszenia",
                "Opis zgłoszenia:",
                "np. Nowy",
                "np. Technik 1"
        );

        // wyświetlenie danych
        tvTitle.setText(sampleTicket.getTitle());
        tvDescription.setText(sampleTicket.getDescription());
        tvStatus.setText("Status: " + sampleTicket.getStatus());
        tvAssignee.setText("Przekieruj do: " + sampleTicket.getAssignee());
        tvDate.setText("Created: " + sampleTicket.getFormattedDate());

        // obsługa przycisku Edytuj
        btnEdit.setOnClickListener(v ->
                Toast.makeText(this, "Edytuj", Toast.LENGTH_SHORT).show());

        // obsługa przycisku Usuń
        btnDelete.setOnClickListener(v ->
                Toast.makeText(this, "Usuń", Toast.LENGTH_SHORT).show());
    }
}
