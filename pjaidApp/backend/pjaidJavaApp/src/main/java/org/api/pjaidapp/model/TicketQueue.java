package org.api.pjaidapp.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TicketQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Ticket ticket;

    private String queueStatus;

    private boolean isUrgent;

    private LocalDateTime queuedAt;

    private Long assignedTechnicianId;

    // Gettery i settery
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Ticket getTicket() { return ticket; }

    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    public String getQueueStatus() { return queueStatus; }

    public void setQueueStatus(String queueStatus) { this.queueStatus = queueStatus; }

    public boolean isUrgent() { return isUrgent; }

    public void setUrgent(boolean urgent) { isUrgent = urgent; }

    public LocalDateTime getQueuedAt() { return queuedAt; }

    public void setQueuedAt(LocalDateTime queuedAt) { this.queuedAt = queuedAt; }

    public Long getAssignedTechnicianId() { return assignedTechnicianId; }

    public void setAssignedTechnicianId(Long assignedTechnicianId) {
        this.assignedTechnicianId = assignedTechnicianId;
    }
}
