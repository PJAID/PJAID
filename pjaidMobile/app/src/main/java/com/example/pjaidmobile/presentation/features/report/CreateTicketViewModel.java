package com.example.pjaidmobile.presentation.features.report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pjaidmobile.data.model.Location;
import com.example.pjaidmobile.data.model.TicketRequest;
import com.example.pjaidmobile.data.model.TicketResponse;
import com.example.pjaidmobile.domain.usecase.CreateTicketUseCase;
import com.example.pjaidmobile.util.LocationProvider;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Callback;

@HiltViewModel
public class CreateTicketViewModel extends ViewModel {

    private final CreateTicketUseCase createTicketUseCase;
    private final LocationProvider locationProvider;

    private final MutableLiveData<Location> locationLiveData = new MutableLiveData<>();

    @Inject
    public CreateTicketViewModel(CreateTicketUseCase createTicketUseCase, LocationProvider locationProvider) {
        this.createTicketUseCase = createTicketUseCase;
        this.locationProvider = locationProvider;
    }

    public void getCurrentLocation() {
        locationProvider.getCurrentLocation(new LocationProvider.LocationCallback() {
            @Override
            public void onLocationReceived(double lat, double lng) {
                locationLiveData.postValue(new Location(lat, lng));
            }

            @Override
            public void onLocationError(String message) {
                // Można dodać osobny LiveData do błędów lokalizacji
            }
        });
    }

    public LiveData<Location> getLocationLiveData() {
        return locationLiveData;
    }

    public void submitTicket(TicketRequest request, Callback<TicketResponse> callback) {
        createTicketUseCase.execute(request, callback);
    }
}
