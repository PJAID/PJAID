package com.example.pjaidmobile.domain.repository;

import com.example.pjaidmobile.data.model.TicketResponse;

public interface TicketRepository {
    TicketResponse getTicketById(String id);
}
