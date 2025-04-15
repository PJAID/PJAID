package com.example.pjaidmobile.domain.usecase;

import android.util.Log;

import com.example.pjaidmobile.data.model.IssueReport;
import com.example.pjaidmobile.domain.repository.ReportRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class SendReportUseCase {
    private static final String TAG = "SendReportUseCase";

    private final ReportRepository reportRepository;

    @Inject
    public SendReportUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
        Log.d(TAG, "Use case initialized with ReportRepository");
    }

    public Completable execute(String deviceId, String description) {
        Log.d(TAG, "Executing report submission with deviceId: " + deviceId + ", description: " + description);

        if (description == null || description.trim().isEmpty()) {
            Log.e(TAG, "Description is invalid: must not be null or empty");
            return Completable.error(new IllegalArgumentException("Description must not be empty"));
        }

        IssueReport report = new IssueReport(deviceId, description);
        Log.i(TAG, "Created IssueReport: " + report);

        return reportRepository.sendReport(report)
                .doOnSubscribe(disposable -> Log.d(TAG, "Sending report to repository"))
                .doOnComplete(() -> Log.i(TAG, "Report sent successfully"))
                .doOnError(throwable -> Log.e(TAG, "Failed to send report: " + throwable.getMessage(), throwable));
    }
}
