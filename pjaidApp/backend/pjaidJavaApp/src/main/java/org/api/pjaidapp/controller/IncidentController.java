package org.api.pjaidapp.controller;

import org.api.pjaidapp.model.Incident;
import org.api.pjaidapp.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    // Endpoint do przypisania technika do incydentu
    @PutMapping("/{incidentId}/assign-technician/{technicianId}")
    public ResponseEntity<Incident> assignTechnician(
            @PathVariable Long incidentId,
            @PathVariable Long technicianId) {

        Incident updatedIncident = incidentService.assignTechnicianToIncident(incidentId, technicianId);
        return ResponseEntity.ok(updatedIncident);
    }

    // Pobierz wszystkie incydenty
    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {
        List<Incident> incidents = incidentService.getAllIncidents();
        return ResponseEntity.ok(incidents);
    }

    // Pobierz incydent po ID
    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        Incident incident = incidentService.getIncidentById(id);
        return ResponseEntity.ok(incident);
    }

    // Stwórz nowy incydent
    @PostMapping
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident) {
        Incident createdIncident = incidentService.createIncident(incident);
        return ResponseEntity.ok(createdIncident);
    }

    // Aktualizuj incydent
    @PutMapping("/{id}")
    public ResponseEntity<Incident> updateIncident(@PathVariable Long id, @RequestBody Incident incidentDetails) {
        Incident updatedIncident = incidentService.updateIncident(id, incidentDetails);
        return ResponseEntity.ok(updatedIncident);
    }

    // Usuń incydent
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }
}

