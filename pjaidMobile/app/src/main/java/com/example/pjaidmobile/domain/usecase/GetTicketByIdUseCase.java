package com.example.pjaidmobile.domain.usecase;
import com.example.pjaidmobile.data.model.TicketResponse;
import com.example.pjaidmobile.domain.repository.TicketRepository;

public class GetTicketByIdUseCase {
    private final TicketRepository repository;

    public GetTicketByIdUseCase(TicketRepository repository) {
        this.repository = repository;
    }

    public TicketResponse execute(String id) {
        return repository.getTicketById(id);
    }
}
