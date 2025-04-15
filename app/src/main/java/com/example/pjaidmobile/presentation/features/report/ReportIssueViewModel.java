package com.example.pjaidmobile.presentation.features.report;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.pjaidmobile.domain.usecase.SendReportUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class ReportIssueViewModel extends ViewModel {
    private static final String TAG = "ReportIssueViewModel";
    private final SendReportUseCase sendReportUseCase;
    private final MutableLiveData<ReportUiState> _uiState = new MutableLiveData<>(ReportUiState.IDLE);
    public final LiveData<ReportUiState> uiState = _uiState; // Publiczny LiveData
    private final CompositeDisposable disposables = new CompositeDisposable(); // Do zarządzania subskrypcjami RxJava

    private String deviceId; // Przechowujemy ID

    @Inject
    public ReportIssueViewModel(SendReportUseCase sendReportUseCase, SavedStateHandle savedStateHandle) {
        this.sendReportUseCase = sendReportUseCase;
        Log.d(TAG, "ViewModel created");
    }

    public void loadInitialData(String intentDeviceId, String name, String sn, String purchase, String lastService) {
        this.deviceId = intentDeviceId;
        String info = "ID: " + deviceId + "\nName: " + name + "\nS/N: " + sn + "\nPurchase: " + purchase + "\nService: " + lastService;
        Log.d(TAG, "Initial data loaded: " + info);
        _uiState.setValue(new ReportUiState.InitialData(info));
    }

    public void submitReport(String description) {
        if (deviceId == null) {
            Log.e(TAG, "Cannot submit report: deviceId is null");
            _uiState.setValue(new ReportUiState.Error("Device ID is missing."));
            return;
        }

        Log.i(TAG, "Submitting report for deviceId=" + deviceId + " with description: " + description);
        _uiState.setValue(ReportUiState.LOADING);

        disposables.add(
                sendReportUseCase.execute(deviceId, description)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    Log.i(TAG, "Report submitted successfully");
                                    _uiState.setValue(ReportUiState.SUCCESS);
                                },
                                throwable -> {
                                    String errorMsg = throwable.getMessage() != null ? throwable.getMessage() : "Unknown error";
                                    Log.e(TAG, "Failed to submit report: " + errorMsg, throwable);
                                    _uiState.setValue(new ReportUiState.Error(errorMsg));
                                }
                        )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
        Log.d(TAG, "ViewModel cleared and disposables disposed");
    }
}
