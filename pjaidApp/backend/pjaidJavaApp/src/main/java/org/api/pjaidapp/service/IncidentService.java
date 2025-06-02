package org.api.pjaidapp.service;

import org.api.pjaidapp.model.Incident;
import org.api.pjaidapp.model.User;
import org.api.pjaidapp.repository.IncidentRepository;
import org.api.pjaidapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {

    public static final String INCIDENT_NOT_FOUND_WITH_ID = "Incident not found with ID: ";
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    public IncidentService(IncidentRepository incidentRepository, UserRepository userRepository) {
        this.incidentRepository = incidentRepository;
        this.userRepository = userRepository;
    }

    public Incident assignTechnicianToIncident(Long incidentId, Long technicianId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException(INCIDENT_NOT_FOUND_WITH_ID + incidentId));

        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found with ID: " + technicianId));


        incident.setTechnician(technician);
        return incidentRepository.save(incident);
    }

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(INCIDENT_NOT_FOUND_WITH_ID + id));
    }

    public Incident createIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    public Incident updateIncident(Long id, Incident incidentDetails) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(INCIDENT_NOT_FOUND_WITH_ID + id));

        incident.setTitle(incidentDetails.getTitle());
        incident.setPriority(incidentDetails.getPriority());
        // ustaw inne pola, jeśli są

        return incidentRepository.save(incident);
    }

    public void deleteIncident(Long id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(INCIDENT_NOT_FOUND_WITH_ID + id));
        incidentRepository.delete(incident);
    }
}

