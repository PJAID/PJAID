package com.example.pjaidmobile;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.pjaidmobile.data.model.Location;
import com.example.pjaidmobile.data.model.TicketRequest;
import com.example.pjaidmobile.data.model.TicketResponse;
import com.example.pjaidmobile.domain.usecase.CreateTicketUseCase;
import com.example.pjaidmobile.presentation.features.report.CreateTicketViewModel;
import com.example.pjaidmobile.util.LocationProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import retrofit2.Callback;

public class CreateTicketViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule(); // Obsługuje LiveData synchronicznie

    private CreateTicketUseCase mockUseCase;
    private LocationProvider mockLocationProvider;
    private CreateTicketViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUseCase = mock(CreateTicketUseCase.class);
        mockLocationProvider = mock(LocationProvider.class);
        viewModel = new CreateTicketViewModel(mockUseCase, mockLocationProvider);
    }

    @Test
    public void getCurrentLocation_shouldPostLocationToLiveData() {
        // Arrange
        double expectedLat = 52.0;
        double expectedLng = 21.0;

        doAnswer(invocation -> {
            LocationProvider.LocationCallback callback = invocation.getArgument(0);
            callback.onLocationReceived(expectedLat, expectedLng);
            return null;
        }).when(mockLocationProvider).getCurrentLocation(any());

        // Act
        viewModel.getCurrentLocation();

        // Assert
        Location result = viewModel.getLocationLiveData().getValue();
        assertNotNull(result);
        assertEquals(expectedLat, result.getLatitude(), 0.001);
        assertEquals(expectedLng, result.getLongitude(), 0.001);
    }

    @Test
    public void submitTicket_shouldCallUseCaseExecute() {
        // Arrange
        TicketRequest request = new TicketRequest("Test tytuł", "Opis", "wysoki", 1, 2, 50.0, 19.0);
        Callback<TicketResponse> callback = mock(Callback.class);

        // Act
        viewModel.submitTicket(request, callback);

        // Assert
        verify(mockUseCase, times(1)).execute(eq(request), eq(callback));
    }

    @Test
    public void getCurrentLocation_shouldNotCrashOnError() {
        // Arrange
        doAnswer(invocation -> {
            LocationProvider.LocationCallback callback = invocation.getArgument(0);
            callback.onLocationError("Błąd GPS");
            return null;
        }).when(mockLocationProvider).getCurrentLocation(any());

        // Act
        viewModel.getCurrentLocation();

        // Assert
        // Tutaj brak wyjątków = test zaliczony
        assertNull(viewModel.getLocationLiveData().getValue());
    }
}