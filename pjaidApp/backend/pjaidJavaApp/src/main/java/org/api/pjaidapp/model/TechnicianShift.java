package org.api.pjaidapp.model;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
public class TechnicianShift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User technician;

    private DayOfWeek weekday;
    private LocalTime timeStart;
    private LocalTime timeEnd;

    // Gettery i settery
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public User getTechnician() { return technician; }

    public void setTechnician(User technician) { this.technician = technician; }

    public DayOfWeek getWeekday() { return weekday; }

    public void setWeekday(DayOfWeek weekday) { this.weekday = weekday; }

    public LocalTime getTimeStart() { return timeStart; }

    public void setTimeStart(LocalTime timeStart) { this.timeStart = timeStart; }

    public LocalTime getTimeEnd() { return timeEnd; }

    public void setTimeEnd(LocalTime timeEnd) { this.timeEnd = timeEnd; }
}
