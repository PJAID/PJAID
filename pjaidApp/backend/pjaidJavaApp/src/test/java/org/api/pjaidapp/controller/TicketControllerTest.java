package org.api.pjaidapp.controller;

import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
import org.api.pjaidapp.exception.TicketNotFoundException;
import org.api.pjaidapp.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    private static final int TEST_TICKET_ID = 1;
    private static final String TEST_TITLE = "Test Title";
    private static final String TEST_DESC = "Test Desc";
    private static final String STATUS_OPEN = "OPEN";
    public static final String NEW_TICKET = "New Ticket";
    public static final String DETAILS = "Details";

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    @Test
    void shouldReturnTicketById() {
        // given
        TicketResponse expected = new TicketResponse(TEST_TICKET_ID, TEST_TITLE, TEST_DESC, STATUS_OPEN);
        when(ticketService.getTicketById(1)).thenReturn(expected);

        // when
        ResponseEntity<TicketResponse> response = ticketController.getTicketById(TEST_TICKET_ID);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
        verify(ticketService, times(1)).getTicketById(TEST_TICKET_ID);
    }

    @Test
    void shouldCreateTicket() {
        // given
        TicketRequest request = new TicketRequest(NEW_TICKET, DETAILS, STATUS_OPEN);
        TicketResponse expected = new TicketResponse(2, NEW_TICKET, DETAILS, STATUS_OPEN);
        when(ticketService.createTicket(request)).thenReturn(expected);

        // when
        ResponseEntity<TicketResponse> response = ticketController.createTicket(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(expected);
        verify(ticketService, times(1)).createTicket(request);
    }

    @Test
    void shouldDeleteTicket() {
        // when
        ticketController.deleteTicket(3);

        // then
        verify(ticketService).deleteTicket(3);
    }

    @Test
    void shouldReturn404WhenTicketNotFound() {
        // given
        when(ticketService.getTicketById(99))
                .thenThrow(new TicketNotFoundException(99));

        // when & then
        assertThrows(TicketNotFoundException.class,
                () -> ticketController.getTicketById(99));
        verify(ticketService, times(1)).getTicketById(99);
    }
}