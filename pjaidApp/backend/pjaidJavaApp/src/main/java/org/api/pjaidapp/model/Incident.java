package org.api.pjaidapp.model;

import jakarta.persistence.*;
import org.api.pjaidapp.enums.Priority;

@Entity
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    // Gettery i settery
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Priority getPriority() { return priority; }

    public void setPriority(Priority priority) { this.priority = priority; }
}
