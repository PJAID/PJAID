package org.api.pjaidapp.model;

import jakarta.persistence.*;

@Entity
public class TechnicianLoginStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long technicianId;

    private boolean isLoggedIn;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getTechnicianId() { return technicianId; }

    public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }

    public boolean isLoggedIn() { return isLoggedIn; }

    public void setLoggedIn(boolean loggedIn) { isLoggedIn = loggedIn; }
}
