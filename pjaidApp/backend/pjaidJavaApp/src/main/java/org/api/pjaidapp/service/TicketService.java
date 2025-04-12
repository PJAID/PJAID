package org.api.pjaidapp.service;

import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
import org.api.pjaidapp.enums.Status;
import org.api.pjaidapp.exception.DeviceNotFoundException;
import org.api.pjaidapp.exception.TicketNotFoundException;
import org.api.pjaidapp.exception.UserNotFoundException;
import org.api.pjaidapp.mapper.TicketMapper;
import org.api.pjaidapp.model.Device;
import org.api.pjaidapp.model.Ticket;
import org.api.pjaidapp.model.User;
import org.api.pjaidapp.repository.DeviceRepository;
import org.api.pjaidapp.repository.TicketRepository;
import org.api.pjaidapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final TicketMapper ticketMapper;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, DeviceRepository deviceRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.ticketMapper = ticketMapper;
    }

    public TicketResponse getTicketById(int id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
        return ticketMapper.toResponse(ticket);
    }

    public List<TicketResponse> getAllActiveTickets() {
        return ticketRepository.findByStatusIn(Arrays.asList(Status.NOWE, Status.W_TRAKCIE))
                .stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    public TicketResponse createTicket(TicketRequest request) {
        Ticket ticket = ticketMapper.toEntity(request);
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new DeviceNotFoundException(request.getDeviceId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));


        ticket.setDevice(device);
        ticket.setUser(user);
        Ticket saved = ticketRepository.save(ticket);

        return ticketMapper.toResponse(saved);
    }

    public void deleteTicket(int id) {
        if (!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException(id);
        }
        ticketRepository.deleteById(id);
    }

    public TicketResponse updateTicket(int id, TicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(request.getStatus());

        ticketRepository.save(ticket);
        return ticketMapper.toResponse(ticket);
    }
}
