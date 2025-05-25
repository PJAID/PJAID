package com.example.pjaidmobile.presentation.features.report;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.data.model.TicketRequest;
import com.example.pjaidmobile.data.model.TicketResponse;
import com.example.pjaidmobile.data.remote.api.TicketApi;
import com.example.pjaidmobile.util.LocationProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class CreateTicketActivity extends AppCompatActivity {
    private GoogleMap googleMap;
    private Double latitude = null;
    private Double longitude = null;
    private MapView mapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Inject
    TicketApi ticketApi;

    @Inject
    LocationProvider locationProvider;

    private final ActivityResultLauncher<String> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (Boolean.TRUE.equals(isGranted)) {
                    enableUserLocation();
                } else {
                    Toast.makeText(this, "Brak zgody na lokalizację", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);
        initViews();
        setupMap(savedInstanceState);
    }

    private void initViews() {
        EditText title = findViewById(R.id.editTextTitle);
        EditText description = findViewById(R.id.editTextDescription);
        Button sendButton = findViewById(R.id.buttonSubmitTicket);
        CheckBox checkboxAddLocation = findViewById(R.id.checkboxAddLocation);
        mapView = findViewById(R.id.mapView);

        setupLocationCheckbox(checkboxAddLocation);
        setupSendButton(title, description, sendButton);
    }

    private void setupMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = savedInstanceState != null ? savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY) : null;
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(map -> {
            googleMap = map;
            checkLocationPermission();
        });
    }

    private void setupLocationCheckbox(CheckBox checkbox) {
        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mapView.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if (isChecked) fetchLocation();
        });
    }

    private void setupSendButton(EditText title, EditText description, Button sendButton) {
        sendButton.setOnClickListener(v -> {
            TicketRequest request = createTicketRequest(
                    title.getText().toString(),
                    description.getText().toString()
            );
            submitTicket(request);
        });
    }

    private TicketRequest createTicketRequest(String title, String description) {
        return new TicketRequest(
                title,
                description,
                "NOWE",
                125,
                2,
                latitude != null ? latitude : 53.1234804d,
                longitude != null ? longitude : 18.004378d
        );
    }

    private void submitTicket(TicketRequest request) {
        ticketApi.createTicket(request).enqueue(new TicketCallback());
    }

    private class TicketCallback implements Callback<TicketResponse> {
        @Override
        public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                handleSuccessResponse(response.body());
            } else {
                handleErrorResponse(response);
            }
        }

        private void handleSuccessResponse(TicketResponse ticketResponse) {
            Toast.makeText(CreateTicketActivity.this,
                    "Zgłoszenie dodane prawidłowo!\nID: " + ticketResponse.getId() +
                            "\nStatus: " + ticketResponse.getStatus(),
                    Toast.LENGTH_LONG).show();
            finish();
        }

        private void handleErrorResponse(Response<TicketResponse> response) {
            String errorMsg = getErrorMessage(response.code());
            Toast.makeText(CreateTicketActivity.this, errorMsg, Toast.LENGTH_LONG).show();
        }

        private void handleFailure(Throwable t) {
            String errorMsg = "Błąd przy dodawaniu zgłoszenia";
            if (t.getMessage() != null) {
                if (t.getMessage().contains("timeout")) {
                    errorMsg = "Przekroczono czas oczekiwania - sprawdź połączenie";
                } else if (t.getMessage().contains("Unable to resolve host")) {
                    errorMsg = "Brak połączenia z internetem";
                } else {
                    errorMsg = "Błąd połączenia: " + t.getMessage();
                }
            }
            Toast.makeText(CreateTicketActivity.this, errorMsg, Toast.LENGTH_LONG).show();
        }

        private String getErrorMessage(int errorCode) {
            switch (errorCode) {
                case 400:
                    return "Nieprawidłowe dane zgłoszenia";
                case 401:
                    return "Brak autoryzacji";
                case 403:
                    return "Brak uprawnień";
                case 500:
                    return "Błąd serwera - spróbuj ponownie";
                default:
                    return "Błąd: " + errorCode;
            }
        }

        @Override
        public void onFailure(Call<TicketResponse> call, Throwable t) {
            handleFailure(t);
        }
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
        } else {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void enableUserLocation() {
        if (googleMap != null && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void fetchLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
            return;
        }

        locationProvider.getCurrentLocation(new LocationProvider.LocationCallback() {
            @Override
            public void onLocationReceived(double lat, double lng) {
                latitude = lat;
                longitude = lng;
                updateMapWithLocation(lat, lng);
            }

            @Override
            public void onLocationError(String message) {
                Toast.makeText(CreateTicketActivity.this, "Błąd lokalizacji: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMapWithLocation(double lat, double lng) {
        if (googleMap != null) {
            LatLng userLocation = new LatLng(lat, lng);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            googleMap.addMarker(new MarkerOptions().position(userLocation).title("Twoja lokalizacja"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
}