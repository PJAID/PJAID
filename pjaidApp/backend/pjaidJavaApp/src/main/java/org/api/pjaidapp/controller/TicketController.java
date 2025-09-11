package org.api.pjaidapp.controller;

import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
import org.api.pjaidapp.enums.Status;
import org.api.pjaidapp.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long id) {
        TicketResponse ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTickets(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String device,
            @RequestParam(required = false) String titleContains
    ) {
        List<TicketResponse> tickets = ticketService.findTicketsByCriteria(status, user, device, titleContains);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<TicketResponse>> getAllActiveTickets() {
        List<TicketResponse> tickets = ticketService.getAllActiveTickets();
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest request) {
        TicketResponse createdTicket = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable Long id, @RequestBody TicketRequest request) {
        TicketResponse updatedTicket = ticketService.updateTicket(id, request);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<TicketResponse> startTicket(@PathVariable Long id) {
        TicketResponse updated = ticketService.startTicket(id);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<TicketResponse> finishTicket(@PathVariable Long id) {
        TicketResponse updated = ticketService.finishTicket(id);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<TicketResponse>> getPendingTickets() {
        List<TicketResponse> tickets = ticketService.getPendingTickets();
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<TicketResponse> assignTechnician(
            @PathVariable Long id,
            @RequestParam Long technicianId) {
        TicketResponse updated = ticketService.assignTechnician(id, technicianId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/assigned")
    public ResponseEntity<List<TicketResponse>> getAssignedTickets(@RequestParam String user) {
        return ResponseEntity.ok(ticketService.getTicketsAssignedTo(user));
    }
    @GetMapping("/status-summary")
    public Map<String, Long> getTicketStatusSummary() {
        return ticketService.getTicketStatusSummary();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<TicketResponse>> getTicketsRelatedToUser(@RequestParam String username) {
        List<TicketResponse> tickets = ticketService.getTicketsByUserOrTechnician(username);
        return ResponseEntity.ok(tickets);
    }
    @GetMapping("/user-or-technician-paged")
    public ResponseEntity<Page<TicketResponse>> getTicketsByUserOrTechnicianPaged(
            @RequestParam String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ticketService.getTicketsByUserOrTechnicianPaged(username, page, size));
    }
    @GetMapping("/paged")
    public ResponseEntity<Page<TicketResponse>> getAllTicketsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ticketService.getAllTicketsPaged(page, size));
    }
    @GetMapping("/mine-paged")
    public ResponseEntity<Page<TicketResponse>> getTicketsMinePaged(
            @RequestParam String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ticketService.getTicketsByUserOrTechnicianPaged(username, page, size));
    }

    @GetMapping("/{id}/report")
    public ResponseEntity<byte[]> exportTicketReport(@PathVariable Long id) {
        byte[] csvData = ticketService.generateCsvReportForTicket(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"ticket_" + id + ".csv\"")
                .contentType(org.springframework.http.MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponse> updateTicketStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> updates
    ) {
        System.out.println("[BACKEND] Otrzymano PATCH /ticket/" + id + "/status z danymi: " + updates);


        if (!updates.containsKey("status")) {
            return ResponseEntity.badRequest().build();
        }


        String newStatus = updates.get("status");
        try {
            Status statusEnum = Status.valueOf(newStatus);
            TicketResponse updatedTicket = ticketService.updateTicketStatus(id, statusEnum);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            System.err.println("[BACKEND] Błędny status: " + newStatus);
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/assignee")
    public ResponseEntity<TicketResponse> updateTicketAssignee(
            @PathVariable Long id,
            @RequestBody Map<String, String> updates
    ) {
        System.out.println("[BACKEND] Otrzymano PATCH /ticket/" + id + "/assignee z danymi: " + updates);

        if (!updates.containsKey("assignee")) {
            return ResponseEntity.badRequest().build();
        }

        String newAssignee = updates.get("assignee");
        try {
            TicketResponse updatedTicket = ticketService.updateTicketAssignee(id, newAssignee);
            return ResponseEntity.ok(updatedTicket);
        } catch (Exception e) {
            System.err.println("[BACKEND] Błąd przypisania technika: " + newAssignee);
            return ResponseEntity.badRequest().build();
        }
    }


}

