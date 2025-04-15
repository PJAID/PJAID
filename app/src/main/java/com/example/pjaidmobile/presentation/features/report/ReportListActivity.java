package com.example.pjaidmobile.presentation.features.report;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjaidmobile.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReportListActivity extends AppCompatActivity {

    private ReportListViewModel viewModel;
    private ReportListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewReports);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReportListAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ReportListViewModel.class);
        viewModel.reports.observe(this, adapter::submitList);

        viewModel.reports.observe(this, reports -> {
            Log.d("ReportListActivity", "Dostałem dane z API: " + reports);
            adapter.submitList(reports);
        });


    }


}