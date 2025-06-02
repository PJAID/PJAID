package org.api.pjaidapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class TicketQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    private Ticket ticket;

    @Column(name = "queue_status")
    private String queueStatus;

    @Column(name = "is_urgent")
    private boolean isUrgent;

    @Column(name = "queued_at")
    private LocalDateTime queuedAt;

    @Column(name = "assigned_technician_id")
    private Long assignedTechnicianId;

}
