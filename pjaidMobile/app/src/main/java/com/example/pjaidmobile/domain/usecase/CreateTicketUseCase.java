package com.example.pjaidmobile.domain.usecase;

import com.example.pjaidmobile.data.model.TicketRequest;
import com.example.pjaidmobile.data.model.TicketResponse;
import com.example.pjaidmobile.domain.repository.TicketRepository;

import javax.inject.Inject;

import retrofit2.Callback;

public class CreateTicketUseCase {

    private final TicketRepository ticketRepository;

    @Inject
    public CreateTicketUseCase(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void execute(TicketRequest request, Callback<TicketResponse> callback) {
        if (callback == null) {
            throw new NullPointerException("Callback must not be null");
        }

        ticketRepository.createTicket(request).enqueue(callback);
    }
}