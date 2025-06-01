package org.api.pjaidapp.service;

import org.api.pjaidapp.model.Incident;
import org.api.pjaidapp.model.User;
import org.api.pjaidapp.repository.IncidentRepository;
import org.api.pjaidapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private UserRepository userRepository;

    public Incident assignTechnicianToIncident(Long incidentId, Long technicianId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found with ID: " + incidentId));

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
                .orElseThrow(() -> new RuntimeException("Incident not found with ID: " + id));
    }

    public Incident createIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    public Incident updateIncident(Long id, Incident incidentDetails) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with ID: " + id));

        incident.setTitle(incidentDetails.getTitle());
        incident.setPriority(incidentDetails.getPriority());
        // ustaw inne pola, jeśli są

        return incidentRepository.save(incident);
    }

    public void deleteIncident(Long id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with ID: " + id));
        incidentRepository.delete(incident);
    }
}

