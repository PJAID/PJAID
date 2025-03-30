package org.api.pjaidapp.controller;

import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
import org.api.pjaidapp.service.TicketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{id}")
    public TicketResponse getTicketById(@PathVariable int id) {
        return ticketService.getTicketById(id);
    }

    @PostMapping
    public TicketResponse createTicket(@RequestBody TicketRequest request) {
        return ticketService.createTicket(request);
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable int id) {
        ticketService.deleteTicket(id);
    }
}
