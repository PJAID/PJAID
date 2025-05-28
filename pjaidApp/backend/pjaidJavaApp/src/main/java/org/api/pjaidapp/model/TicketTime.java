package org.api.pjaidapp.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TicketTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User technician;

    @ManyToOne
    private Ticket ticket;

    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;

    // Gettery i settery
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public User getTechnician() { return technician; }

    public void setTechnician(User technician) { this.technician = technician; }

    public Ticket getTicket() { return ticket; }

    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    public LocalDateTime getTimeStart() { return timeStart; }

    public void setTimeStart(LocalDateTime timeStart) { this.timeStart = timeStart; }

    public LocalDateTime getTimeEnd() { return timeEnd; }

    public void setTimeEnd(LocalDateTime timeEnd) { this.timeEnd = timeEnd; }
}
