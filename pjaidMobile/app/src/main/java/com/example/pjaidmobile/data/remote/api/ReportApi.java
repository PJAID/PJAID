package com.example.pjaidmobile.data.remote.api;

import com.example.pjaidmobile.data.model.IssueReport;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReportApi {
    @POST("/reports")
    Call<Void> sendReport(@Body IssueReport report);
}
