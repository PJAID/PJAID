package org.api.pjaidapp.repository;

import org.api.pjaidapp.model.Ticket;
import org.api.pjaidapp.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByTitleContainingIgnoreCase(String title);


}
