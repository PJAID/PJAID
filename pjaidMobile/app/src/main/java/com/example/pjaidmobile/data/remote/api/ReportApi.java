package com.example.pjaidmobile.data.remote.api;

import com.example.pjaidmobile.data.model.IssueReport;

import io.reactivex.rxjava3.core.Completable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReportApi {
    @POST("/reports")
    Completable sendReport(@Body IssueReport report);
}
