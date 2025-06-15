package com.example.pjaidmobile.presentation.features.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pjaidmobile.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReportDetailFragment extends Fragment {

    private ReportDetailViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ReportDetailViewModel.class);

        String reportId = ReportDetailFragmentArgs.fromBundle(getArguments()).getReportId();
        viewModel.loadReportById(reportId);

        viewModel.getReport().observe(getViewLifecycleOwner(), report -> {
            TextView titleView = view.findViewById(R.id.report_title);
            titleView.setText(report.getTitle());
        });
    }
}
