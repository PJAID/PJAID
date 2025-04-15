package com.example.pjaidmobile.domain.usecase;
import com.example.pjaidmobile.data.model.Ticket;
import com.example.pjaidmobile.domain.repository.TicketRepository;

public class GetTicketByIdUseCase {
    private final TicketRepository repository;

    public GetTicketByIdUseCase(TicketRepository repository) {
        this.repository = repository;
    }

    public Ticket execute(String id) {
        return repository.getTicketById(id);
    }
}
