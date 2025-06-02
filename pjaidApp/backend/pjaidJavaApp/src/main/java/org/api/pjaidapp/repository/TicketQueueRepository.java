package org.api.pjaidapp.repository;

import org.api.pjaidapp.model.TicketQueue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketQueueRepository extends JpaRepository<TicketQueue, Long> {
    List<TicketQueue> findByQueueStatus(String status);
}
