package com.example.pjaidmobile.domain.repository;

import com.example.pjaidmobile.data.model.IssueReport;
import com.example.pjaidmobile.data.model.ReportItem;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface ReportRepository {
    Completable sendReport(IssueReport report);

    Single<List<ReportItem>> getAllReports();
}
