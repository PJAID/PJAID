package org.api.pjaidapp.exception;

public class IncidentNotFoundException extends RuntimeException {

    public IncidentNotFoundException(Long id) {
        super("Incident not found with ID: " + id);
    }
}
