package org.api.pjaidapp.repository;

import org.api.pjaidapp.model.TicketTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketTimeRepository extends JpaRepository<TicketTime, Long> {
    List<TicketTime> findByTechnicianId(Long technicianId);
}

