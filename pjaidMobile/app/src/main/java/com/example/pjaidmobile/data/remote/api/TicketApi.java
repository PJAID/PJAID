package com.example.pjaidmobile.data.remote.api;

import com.example.pjaidmobile.data.model.TicketResponse;
import com.example.pjaidmobile.data.model.TicketRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TicketApi {
    @GET("ticket")
    Call<List<TicketResponse>> getAllTickets();

    @GET("ticket/{id}")
    Call<TicketResponse> getTicket(@Path("id") int id);

    @POST("ticket")
    Call<TicketResponse> createTicket(@Body TicketRequest ticketRequest);

    @GET("ticket/{id}")
    Call<TicketResponse> getTicketById(@Path("id") String id);
}