package com.example.pjaidmobile.domain.repository;

import com.example.pjaidmobile.data.model.Ticket;

public interface TicketRepository {
    Ticket getTicketById(String id);
}
