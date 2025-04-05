package org.api.pjaidapp.service;

import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
import org.api.pjaidapp.exception.TicketNotFoundException;
import org.api.pjaidapp.mapper.TicketMapper;
import org.api.pjaidapp.model.Ticket;
import org.api.pjaidapp.model.TicketStatus;
import org.api.pjaidapp.repository.TicketRepository;
import org.springframework.stereotype.Service;

import org.api.pjaidapp.dto.TicketStatusUpdateDTO;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public TicketService(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
        return ticketMapper.toResponse(ticket);
    }

    public TicketResponse createTicket(TicketRequest request) {
        Ticket ticket = ticketMapper.toEntity(request);
        Ticket saved = ticketRepository.save(ticket);
        return ticketMapper.toResponse(saved);
    }

    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException(id);
        }
        ticketRepository.deleteById(id);
    }
    public TicketResponse updateTicketStatus(TicketStatusUpdateDTO updateDTO) {
        Ticket ticket = ticketRepository.findById(updateDTO.getId())
                .orElseThrow(() -> new TicketNotFoundException(updateDTO.getId()));

        try {
            TicketStatus newStatus = TicketStatus.valueOf(updateDTO.getStatus());
            ticket.setStatus(newStatus);
            // (opcjonalnie) logika komentarza itd.
            Ticket saved = ticketRepository.save(ticket);
            return ticketMapper.toResponse(saved);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nieprawidłowy status: " + updateDTO.getStatus());
        }
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

}



