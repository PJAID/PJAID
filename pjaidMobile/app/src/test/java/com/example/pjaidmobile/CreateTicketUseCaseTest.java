package com.example.pjaidmobile;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.pjaidmobile.data.model.TicketRequest;
import com.example.pjaidmobile.data.model.TicketResponse;
import com.example.pjaidmobile.domain.repository.TicketRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import retrofit2.Call;
import retrofit2.Callback;

public class CreateTicketUseCaseTest {

    @Mock
    TicketRepository ticketRepository;

    @Mock
    Call<TicketResponse> mockCall;

    @Mock
    Callback<TicketResponse> callback;

    private CreateTicketUseCase useCase;
    private AutoCloseable closeable;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        useCase = new CreateTicketUseCase(ticketRepository);
    }

    @After
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testExecute_shouldCallRepositoryAndEnqueueCallback() {
        // Given
        TicketRequest request = new TicketRequest("Tytuł", "Opis", "NOWE", 1, 2, 53.0, 18.0);

        when(ticketRepository.createTicket(request)).thenReturn(mockCall);

        // When
        useCase.execute(request, callback);

        // Then
        verify(ticketRepository).createTicket(request);
        verify(mockCall).enqueue(callback);
    }

    @Test(expected = NullPointerException.class)
    public void testExecute_shouldThrowExceptionWhenRepositoryReturnsNull() {
        // Given
        TicketRequest request = new TicketRequest("Tytuł", "Opis", "NOWE", 1, 2, 53.0, 18.0);

        when(ticketRepository.createTicket(request)).thenReturn(null);

        // When
        useCase.execute(request, callback);
    }

    @Test
    public void testExecute_shouldThrowExceptionWhenCallbackIsNull() {
        TicketRequest request = new TicketRequest("Tytuł", "Opis", "NOWE", 1, 2, 53.0, 18.0);
        when(ticketRepository.createTicket(request)).thenReturn(mockCall);

        assertThrows(NullPointerException.class, () -> useCase.execute(request, null));
    }


}