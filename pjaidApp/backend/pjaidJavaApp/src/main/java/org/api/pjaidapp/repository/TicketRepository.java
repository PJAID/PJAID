package org.api.pjaidapp.repository;


import org.api.pjaidapp.enums.Status;
import org.api.pjaidapp.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByStatusIn(Collection<Status> status);
}

