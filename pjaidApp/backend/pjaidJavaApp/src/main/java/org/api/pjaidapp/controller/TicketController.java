package org.api.pjaidapp.controller;

import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
import org.api.pjaidapp.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{id}")
    public String getTicketById(@PathVariable int id, Model model) {
        TicketResponse ticket = ticketService.getTicketById(id);
        model.addAttribute("ticket", ticket);
        return "ticket-details"; // nazwa szablonu .html
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