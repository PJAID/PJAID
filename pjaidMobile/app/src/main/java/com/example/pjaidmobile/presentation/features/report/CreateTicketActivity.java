package com.example.pjaidmobile.presentation.features.ticket;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.data.model.TicketRequest;
import com.example.pjaidmobile.data.remote.api.TicketApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class CreateTicketActivity extends AppCompatActivity {

    @Inject
    TicketApi ticketApi;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private String selectedHala = null;
    private Double locationX = null;
    private Double locationY = null;

    private TextView locationText;
    private Spinner spinner;

    private FusedLocationProviderClient fusedLocationClient;

    // Klasy do reprezentacji hali i jej rogów
    static class Corner {
        double latitude;
        double longitude;
        Corner(double lat, double lon) {
            this.latitude = lat;
            this.longitude = lon;
        }
    }

    static class Hala {
        int id;
        String name;
        List<Corner> corners;
        Hala(int id, String name, List<Corner> corners) {
            this.id = id;
            this.name = name;
            this.corners = corners;
        }
    }

    // Lista hal
    private List<Hala> halls = Arrays.asList(
            new Hala(1, "Hala B1", Arrays.asList(
                    new Corner(54.1122, 18.7973),
                    new Corner(54.1128, 18.7995),
                    new Corner(54.1116, 18.8002),
                    new Corner(54.1111, 18.7980)
            )),
            new Hala(2, "Hala B2", Arrays.asList(
                    new Corner(54.1117, 18.7949),
                    new Corner(54.1122, 18.7969),
                    new Corner(54.1109, 18.7977),
                    new Corner(54.1105, 18.7957)
            )),
            new Hala(3, "Hala B3", Arrays.asList(
                    new Corner(54.1111, 18.7930),
                    new Corner(54.1115, 18.7945),
                    new Corner(54.1104, 18.7953),
                    new Corner(54.1101, 18.7938)
            )),
            new Hala(4, "Hala B4", Arrays.asList(
                    new Corner(54.1108, 18.7911),
                    new Corner(54.1110, 18.7921),
                    new Corner(54.1099, 18.7930),
                    new Corner(54.1097, 18.7918)
            )),
            new Hala(5, "Hala B5", Arrays.asList(
                    new Corner(54.1103, 18.7891),
                    new Corner(54.1107, 18.7910),
                    new Corner(54.1109, 18.7918),
                    new Corner(54.1092, 18.7898)
            ))
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);
        EditText title = findViewById(R.id.editTextTitle);
        EditText description = findViewById(R.id.editTextDescription);
        locationText = findViewById(R.id.textViewLocation);
        spinner = findViewById(R.id.spinnerLocationOptions);
        Button sendButton = findViewById(R.id.buttonSubmitTicket);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        requestLocationPermissionAndFetch();

        sendButton.setOnClickListener(v -> {
            if (locationX == null || locationY == null) {
                if (selectedHala == null) {
                    Toast.makeText(this, "Nie podano lokalizacji zgłoszenia", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            TicketRequest request = new TicketRequest(
                    title.getText().toString(),
                    description.getText().toString(),
                    "NOWE",
                    125,
                    44,
                    locationX,
                    locationY
            );

            ticketApi.createTicket(request).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CreateTicketActivity.this, "Zgłoszenie dodane!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CreateTicketActivity.this, "Błąd serwera", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(CreateTicketActivity.this, "Niepowodzenie: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void requestLocationPermissionAndFetch() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fetchLocation();
        }
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        locationX = location.getLatitude();
                        locationY = location.getLongitude();

                        // Sprawdź, czy lokalizacja jest w którejś hali
                        Hala hala = findHalaForLocation(locationX, locationY);
                        if (hala != null) {
                            selectedHala = hala.name;
                            locationText.setText("Lokalizacja: " + selectedHala);
                            // Ustaw centroid hali jako lokalizację zgłoszenia
                            Corner centroid = getCentroid(hala.corners);
                            locationX = centroid.latitude;
                            locationY = centroid.longitude;
                            spinner.setVisibility(View.GONE);
                        } else {
                            // Lokalizacja poza halami - pokaż spinner do wyboru
                            showSpinner();
                            locationText.setText("Lokalizacja poza halami, wybierz halę ręcznie");
                        }
                    } else {
                        showSpinner();
                    }
                });
    }

    // Metoda sprawdzająca, czy punkt jest wewnątrz wielokąta
    private boolean isPointInPolygon(double lat, double lon, List<Corner> polygon) {
        int intersectCount = 0;
        for (int i = 0; i < polygon.size(); i++) {
            Corner a = polygon.get(i);
            Corner b = polygon.get((i + 1) % polygon.size());

            if (((a.latitude > lat) != (b.latitude > lat)) &&
                    (lon < (b.longitude - a.longitude) * (lat - a.latitude) / (b.latitude - a.latitude) + a.longitude)) {
                intersectCount++;
            }
        }
        return (intersectCount % 2) == 1;
    }

    // Znajdź halę dla danej lokalizacji lub null jeśli brak
    private Hala findHalaForLocation(double lat, double lon) {
        for (Hala h : halls) {
            if (isPointInPolygon(lat, lon, h.corners)) {
                return h;
            }
        }
        return null;
    }

    // Oblicz centroid wielokąta (prosty średni)
    private Corner getCentroid(List<Corner> polygon) {
        double latSum = 0;
        double lonSum = 0;
        int n = polygon.size();
        for (Corner c : polygon) {
            latSum += c.latitude;
            lonSum += c.longitude;
        }
        return new Corner(latSum / n, lonSum / n);
    }

    private void showSpinner() {
        spinner.setVisibility(View.VISIBLE);
        String[] hale = {"Wybierz halę", "Hala B1", "Hala B2", "Hala B3", "Hala B4", "Hala B5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, hale
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                if (!selected.equals("Wybierz halę")) {
                    selectedHala = selected;
                    locationText.setText("Lokalizacja: " + selectedHala);

                    // Przypisz współrzędne hali ręcznie do locationX, locationY
                    Hala hala = halls.stream()
                            .filter(h -> h.name.equals(selected))
                            .findFirst()
                            .orElse(null);
                    if (hala != null) {
                        Corner centroid = getCentroid(hala.corners);
                        locationX = centroid.latitude;
                        locationY = centroid.longitude;
                    } else {
                        locationX = null;
                        locationY = null;
                    }
                } else {
                    selectedHala = null;
                    locationText.setText("Nie można pobrać lokalizacji, wybierz halę ręcznie");
                    locationX = null;
                    locationY = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                showSpinner();
            }
        }
    }
}
