package com.example.pjaidmobile.domain.repository;

import com.example.pjaidmobile.data.model.TicketRequest;
import com.example.pjaidmobile.data.model.TicketResponse;

import retrofit2.Call;

public interface TicketRepository {
    TicketResponse getTicketById(String id);
    Call<TicketResponse> createTicket(TicketRequest request);
}
