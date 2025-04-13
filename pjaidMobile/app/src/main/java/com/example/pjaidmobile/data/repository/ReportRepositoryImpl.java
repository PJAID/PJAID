package com.example.pjaidmobile.data.repository;

import com.example.pjaidmobile.data.model.IssueReport;
import com.example.pjaidmobile.data.model.ReportItem;
import com.example.pjaidmobile.data.remote.api.ReportApi;
import com.example.pjaidmobile.domain.repository.ReportRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class ReportRepositoryImpl implements ReportRepository {

    private final ReportApi reportApi;

    @Inject
    public ReportRepositoryImpl(ReportApi reportApi) {
        this.reportApi = reportApi;
    }

    @Override
    public Completable sendReport(IssueReport report) {
        return reportApi.sendReport(report);
    }


    @Override
    public Single<List<ReportItem>> getAllReports() {
        return reportApi.getAllReports();
    }
}
