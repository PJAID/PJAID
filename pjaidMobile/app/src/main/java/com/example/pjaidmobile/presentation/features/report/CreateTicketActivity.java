package com.example.pjaidmobile.presentation.features.report;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.data.model.Device;
import com.example.pjaidmobile.data.model.TicketRequest;
import com.example.pjaidmobile.data.model.TicketResponse;
import com.example.pjaidmobile.util.DeviceIntentHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class CreateTicketActivity extends AppCompatActivity {
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private CreateTicketViewModel viewModel;
    private GoogleMap googleMap;
    private MapView mapView;
    private Double latitude = null;
    private Double longitude = null;

    private final ActivityResultLauncher<String> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (Boolean.TRUE.equals(isGranted)) {
                    enableUserLocation();
                    viewModel.getCurrentLocation();
                } else {
                    Toast.makeText(this, "Brak zgody na lokalizację", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);

        viewModel = new ViewModelProvider(this).get(CreateTicketViewModel.class);
        mapView = findViewById(R.id.mapView);
        setupMap(savedInstanceState);
        setupForm();
        observeViewModel();

        Device device = DeviceIntentHelper.extractDeviceFromIntent(getIntent());
        populateFormFromDevice(device);
    }

    private void observeViewModel() {
        viewModel.getLocationLiveData().observe(this, location -> {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            updateMapWithLocation(latitude, longitude);
        });
    }

    private void setupMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = savedInstanceState != null ? savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY) : null;
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(map -> {
            googleMap = map;
            checkLocationPermission();
        });
    }

    private void setupForm() {
        EditText title = findViewById(R.id.editTextTitle);
        EditText description = findViewById(R.id.editTextDescription);
        Button sendButton = findViewById(R.id.buttonSubmitTicket);

        sendButton.setOnClickListener(v -> {
            TicketRequest request = new TicketRequest(
                    title.getText().toString(),
                    description.getText().toString(),
                    "NOWE",
                    125, // TODO: dynamiczne userId
                    2,   // TODO: dynamiczne deviceId
                    latitude != null ? latitude : 53.1234804d,
                    longitude != null ? longitude : 18.004378d
            );
            viewModel.submitTicket(request, new TicketCallback());
        });
    }

    private void populateFormFromDevice(Device device) {
        EditText titleEditText = findViewById(R.id.editTextTitle);
        EditText descriptionEditText = findViewById(R.id.editTextDescription);

        if (device.getName() != null) {
            titleEditText.setText(getString(R.string.ticket_title_prefix, device.getName()));
        }

        StringBuilder descriptionBuilder = new StringBuilder();
        if (device.getSerialNumber() != null) {
            descriptionBuilder.append("Numer seryjny: ").append(device.getSerialNumber()).append("\n");
        }
        if (device.getPurchaseDate() != null) {
            descriptionBuilder.append("Data zakupu: ").append(device.getPurchaseDate());
        }

        if (descriptionBuilder.length() > 0) {
            descriptionEditText.setText(descriptionBuilder.toString());
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
            viewModel.getCurrentLocation();
        } else {
            locationPermissionRequest.launch(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void enableUserLocation() {
        if (googleMap != null && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
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

    private class TicketCallback implements Callback<TicketResponse> {
        private final TicketCallbackHandler handler = new TicketCallbackHandler(CreateTicketActivity.this);

        @Override
        public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                handler.onSuccess(response.body());
            } else {
                handler.onError(response);
            }
        }

        @Override
        public void onFailure(Call<TicketResponse> call, Throwable t) {
            handler.onFailure(t);
        }
    }
}
