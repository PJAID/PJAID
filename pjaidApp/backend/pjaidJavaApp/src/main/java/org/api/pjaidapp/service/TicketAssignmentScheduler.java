package org.api.pjaidapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TicketAssignmentScheduler {

    @Autowired
    private TicketAssignmentService ticketAssignmentService;

    @Scheduled(fixedRate = 300_000) // every 5 minutes
    public void scheduleTicketAssignment() {
        ticketAssignmentService.assignPendingTickets();
    }
}
