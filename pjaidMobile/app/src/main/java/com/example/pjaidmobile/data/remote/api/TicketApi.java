package com.example.pjaidmobile.data.remote.api;

import com.example.pjaidmobile.data.model.Ticket;
import com.example.pjaidmobile.data.model.TicketRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TicketApi {
    @GET("ticket")
    Call<List<Ticket>> getAllTickets();

    @GET("ticket/{id}")
    Call<Ticket> getTicket(@Path("id") int id);

    @POST("ticket")
    Call<Ticket> createTicket(@Body TicketRequest ticketRequest);

    @GET("ticket/{id}")
    Call<Ticket> getTicketById(@Path("id") String id);
}