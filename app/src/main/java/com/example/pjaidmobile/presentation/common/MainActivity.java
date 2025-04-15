package com.example.pjaidmobile.presentation.common;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.presentation.features.report.ReportIssueActivity;
import com.example.pjaidmobile.presentation.features.report.ReportListActivity;
import com.example.pjaidmobile.presentation.features.scan.ScanQRActivity;
import com.example.pjaidmobile.presentation.features.ticket.CreateTicketActivity;
import com.example.pjaidmobile.presentation.features.ticket.TicketDetailActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scanQR = findViewById(R.id.buttonScanQR);
        Button reportIssue = findViewById(R.id.buttonReportIssue);
        Button reportList = findViewById(R.id.buttonReportList);
        Button reportDetail = findViewById(R.id.buttonReportDetail);

        scanQR.setOnClickListener(v ->
                startActivity(new Intent(this, ScanQRActivity.class)));

        reportIssue.setOnClickListener(v ->
                startActivity(new Intent(this, CreateTicketActivity.class)));

        reportList.setOnClickListener(v ->
                startActivity(new Intent(this, ReportListActivity.class)));

        reportDetail.setOnClickListener(v -> {
            Intent intent = new Intent(this, TicketDetailActivity.class);
            intent.putExtra("TICKET_ID", "TK001"); //  przykładowe ID
            startActivity(intent);
        });
    }
}
