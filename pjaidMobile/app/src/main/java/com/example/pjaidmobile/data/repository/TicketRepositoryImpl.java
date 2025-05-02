package com.example.pjaidmobile.data.repository;

import com.example.pjaidmobile.data.model.Ticket;
import com.example.pjaidmobile.data.remote.api.TicketApi;
import com.example.pjaidmobile.domain.repository.TicketRepository;

import java.io.IOException;

import retrofit2.Response;

public class TicketRepositoryImpl implements TicketRepository {
    private final TicketApi ticketApi;

    public TicketRepositoryImpl(TicketApi ticketApi) {
        this.ticketApi = ticketApi;
    }

    @Override
    public Ticket getTicketById(String id) {
        try {
            Response<Ticket> response = ticketApi.getTicketById(id).execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                throw new RuntimeException("API error: " + response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException("Network error", e);
        }
    }
}
