package com.example.myapplication.ui.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentReportIssueBinding;

public class ReportIssueFragment extends Fragment {

    private FragmentReportIssueBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportIssueBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] issueTypes = {"Wybierz typ", "Awaria 1", "Awaria 2"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, issueTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerIssueType.setAdapter(adapter);

        binding.btnSubmit.setOnClickListener(v -> {
            String deviceInfo = binding.editDeviceInfo.getText().toString();
            String issueDesc = binding.editIssueDesc.getText().toString();
            Object selectedItem = binding.spinnerIssueType.getSelectedItem();
            String issueType = selectedItem != null ? selectedItem.toString() : "Brak wyboru";

            // backend tu bedzie
            Toast.makeText(requireContext(), "Zgłoszono awarię:\n" + deviceInfo + "\n" + issueDesc + "\nTyp: " + issueType, Toast.LENGTH_LONG).show();

            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.navigation_home);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}