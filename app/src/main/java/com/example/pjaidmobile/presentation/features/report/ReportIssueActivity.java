package com.example.pjaidmobile.presentation.features.report;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.util.Constants;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReportIssueActivity extends AppCompatActivity {
    private static final String TAG = "ReportIssueActivity";

    EditText editTextDescription;
    TextView textViewDeviceInfo;
    Button buttonSubmit;

    private ReportIssueViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);
        Log.d(TAG, "onCreate: Activity started");

        viewModel = new ViewModelProvider(this).get(ReportIssueViewModel.class);

        editTextDescription = findViewById(R.id.editTextDescription);
        textViewDeviceInfo = findViewById(R.id.textViewDeviceInfo);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        String intentDeviceId = getIntent().getStringExtra(Constants.KEY_DEVICE_ID);
        String name = getIntent().getStringExtra(Constants.KEY_DEVICE_NAME);
        String sn = getIntent().getStringExtra(Constants.KEY_SERIAL_NUMBER);
        String purchase = getIntent().getStringExtra(Constants.KEY_PURCHASE_DATE);
        String lastService = getIntent().getStringExtra(Constants.KEY_LAST_SERVICE);

        Log.d(TAG, "Received device info from Intent: id=" + intentDeviceId + ", name=" + name + ", SN=" + sn);

        viewModel.loadInitialData(intentDeviceId, name, sn, purchase, lastService);
        Log.i(TAG, "Initial data passed to ViewModel");

        buttonSubmit.setOnClickListener(v -> {
            String description = editTextDescription.getText().toString();
            if (description.isEmpty()) {
                Toast.makeText(this, R.string.error_description_empty, Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Submit button clicked with empty description");
            } else {
                Log.d(TAG, "Submitting report with description: " + description);
                viewModel.submitReport(description);
            }
        });

        viewModel.uiState.observe(this, this::handleUiState);
        Log.d(TAG, "UI state observer attached");
    }

    private void handleUiState(ReportUiState state) {
        buttonSubmit.setEnabled(!(state instanceof ReportUiState.Loading));

        if (state instanceof ReportUiState.InitialData) {
            textViewDeviceInfo.setText(((ReportUiState.InitialData) state).deviceInfo);
            Log.d(TAG, "Device info displayed on screen");
        } else if (state instanceof ReportUiState.Success) {
            Toast.makeText(this, R.string.report_sent_success, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Report submitted successfully");
            finish();
        } else if (state instanceof ReportUiState.Error) {
            String message = ((ReportUiState.Error) state).message;
            String errorMessage = message != null ? message : getString(R.string.unknown_error);
            Toast.makeText(this, getString(R.string.report_error_prefix) + errorMessage, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error while submitting report: " + errorMessage);
        }
    }
}