package com.example.pjaidmobile.data.remote.api;

import com.example.pjaidmobile.data.model.IssueReport;
import com.example.pjaidmobile.data.model.ReportItem;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ReportApi {
    @POST("/reports")
    Completable sendReport(@Body IssueReport report);

    @GET("/ticket")
    Single<List<ReportItem>> getAllReports();
}
