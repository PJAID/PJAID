package org.api.pjaidapp.service;

import org.api.pjaidapp.dto.DeviceDto;
import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
import org.api.pjaidapp.dto.UserDto;
import org.api.pjaidapp.enums.Status;
import org.api.pjaidapp.exception.DeviceNotFoundException;
import org.api.pjaidapp.exception.TicketNotFoundException;
import org.api.pjaidapp.exception.UserNotFoundException;
import org.api.pjaidapp.mapper.TicketMapper;
import org.api.pjaidapp.model.Device;
import org.api.pjaidapp.model.Ticket;
import org.api.pjaidapp.repository.DeviceRepository;
import org.api.pjaidapp.repository.TicketRepository;
import org.api.pjaidapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

    @ExtendWith(MockitoExtension.class)
    class TicketServiceTest {

        @InjectMocks
        private TicketService ticketService;

        @Mock
        private TicketRepository ticketRepository;

        @Mock
        private DeviceRepository deviceRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private TicketMapper ticketMapper;

        @Test
        void shouldGetTicketByIdSuccessfully() {
            // arrange
            Ticket ticket = new Ticket();
            ticket.setId(1L);
            TicketResponse response = new TicketResponse(
                    1,
                    "Test title",
                    "Test description",
                    Status.NOWE,
                    null,
                    null
            );
            when(ticketRepository.findById(1)).thenReturn(Optional.of(ticket));
            when(ticketMapper.toResponse(ticket)).thenReturn(response);

            // act
            TicketResponse result = ticketService.getTicketById(1);

            // assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1);
            assertThat(result.getTitle()).isEqualTo("Test title");
            assertThat(result.getStatus()).isEqualTo(Status.NOWE);

            verify(ticketRepository, times(1)).findById(1);
            verify(ticketMapper, times(1)).toResponse(ticket);
        }

        @Test
        void shouldThrowWhenTicketNotFoundById() {
            // arrange
            when(ticketRepository.findById(1)).thenReturn(Optional.empty());

            // act & asser
            assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById(1));
            verify(ticketRepository, times(1)).findById(1);
        }
        @Test
        void shouldThrowWhenDeviceNotFoundDuringCreate() {
            // arrange
            TicketRequest request = new TicketRequest("Test title", "Test desc", Status.NOWE, 1L, 1L);
            when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

            // act & assert
            assertThrows(DeviceNotFoundException.class, () -> ticketService.createTicket(request));
        }

        @Test
        void shouldThrowWhenUserNotFoundDuringCreate() {
            // arrange
            TicketRequest request = new TicketRequest("Test title", "Test desc", Status.NOWE, 1L, 1L);
            Device device = new Device();
            when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            // act & assert
            assertThrows(UserNotFoundException.class, () -> ticketService.createTicket(request));
        }

        @Test
        void shouldDeleteTicketSuccessfully() {
            // arrange
            int ticketId = 1;
            when(ticketRepository.existsById(ticketId)).thenReturn(true);

            // act
            ticketService.deleteTicket(ticketId);

            // assert
            verify(ticketRepository, times(1)).deleteById(ticketId);
        }

        @Test
        void shouldThrowWhenDeletingNonExistingTicket() {
            // arrange
            int ticketId = 1;
            when(ticketRepository.existsById(ticketId)).thenReturn(false);

            // act & assert
            assertThrows(TicketNotFoundException.class, () -> ticketService.deleteTicket(ticketId));
        }

        @Test
        void shouldUpdateTicketSuccessfully() {
            // arrange
            int ticketId = 1;
            TicketRequest request = new TicketRequest("Updated title", "Updated desc", Status.W_TRAKCIE, 1L, 1L);
            Ticket ticket = new Ticket();
            ticket.setId(Long.valueOf(ticketId));

            when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
            when(ticketMapper.toResponse(ticket)).thenReturn(new TicketResponse(ticketId, "Updated title", "Updated desc", Status.W_TRAKCIE, new DeviceDto(), new UserDto()));

            // act
            TicketResponse response = ticketService.updateTicket(ticketId, request);

            // assert
            assertThat(response.getTitle()).isEqualTo("Updated title");
            verify(ticketRepository, times(1)).save(ticket);
        }

        @Test
        void shouldFindAllActiveTickets() {
            // arrange
            Ticket ticket1 = new Ticket();
            ticket1.setId(1L);
            ticket1.setStatus(Status.NOWE);

            Ticket ticket2 = new Ticket();
            ticket2.setId(2L);
            ticket2.setStatus(Status.W_TRAKCIE);

            when(ticketRepository.findByStatusIn(Arrays.asList(Status.NOWE, Status.W_TRAKCIE)))
                    .thenReturn(Arrays.asList(ticket1, ticket2));
            when(ticketMapper.toResponse(any())).thenReturn(new TicketResponse());

            // act
            List<TicketResponse> activeTickets = ticketService.getAllActiveTickets();

            // assert
            assertThat(activeTickets).hasSize(2);
        }
    }