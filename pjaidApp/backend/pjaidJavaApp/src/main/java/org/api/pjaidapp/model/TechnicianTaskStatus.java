package org.api.pjaidapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class TechnicianTaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User technician;

    private String taskStatus;

    private LocalDateTime timeStart;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public User getTechnician() { return technician; }

    public void setTechnician(User technician) { this.technician = technician; }

    public String getTaskStatus() { return taskStatus; }

    public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; }

    public LocalDateTime getTimeStart() { return timeStart; }

    public void setTimeStart(LocalDateTime timeStart) { this.timeStart = timeStart; }
}
