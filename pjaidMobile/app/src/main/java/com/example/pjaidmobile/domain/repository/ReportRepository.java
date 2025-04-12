package com.example.pjaidmobile.domain.repository;

import com.example.pjaidmobile.data.model.IssueReport;

import io.reactivex.rxjava3.core.Completable;

public interface ReportRepository {
    Completable sendReport(IssueReport report);
}
