package com.example.pjaidmobile.presentation.features.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjaidmobile.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReportListFragment extends Fragment {

    private ReportListViewModel viewModel;
    private ReportListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewReports);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ReportListAdapter();

        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ReportListViewModel.class);
        viewModel.reports.observe(getViewLifecycleOwner(), adapter::submitList);
    }
}
