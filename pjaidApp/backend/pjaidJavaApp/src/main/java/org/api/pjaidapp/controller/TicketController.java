package org.api.pjaidapp.controller;

import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
import org.api.pjaidapp.enums.Status;
import org.api.pjaidapp.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}

