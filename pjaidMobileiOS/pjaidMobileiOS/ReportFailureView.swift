//
//  ReportFailureView.swift
//  pjaidMobileiOS
//
//  Created by Adrian Goik on 28/04/2025.
//


import SwiftUI
import CoreLocation
import Foundation


enum TicketStatus: String, CaseIterable, Identifiable {
    case nowe = "NOWE"
    case przestoj = "PRZESTÓJ"

    var id: String { self.rawValue }
}

struct PolygonBuilding: Identifiable {
let id: Int
let name: String
let corners: [CLLocationCoordinate2D]
}

// MARK: - Główny widok
struct ReportFailureView: View {
    @EnvironmentObject var appState: AppState
    @StateObject private var locationManager = LocationManager()
    
    // Konfiguracja poligonów hal
    let polygonBuildings: [PolygonBuilding] = [
        PolygonBuilding(
            id: 1,
            name: "Hala B1",
            corners: [
                CLLocationCoordinate2D(latitude: 54.1122, longitude: 18.7973),
                CLLocationCoordinate2D(latitude: 54.1128, longitude: 18.7995),
                CLLocationCoordinate2D(latitude: 54.1116, longitude: 18.8002),
                CLLocationCoordinate2D(latitude: 54.1111, longitude: 18.7980)
            ]
        ),
        PolygonBuilding(
            id: 2,
            name: "Hala B2",
            corners: [
                CLLocationCoordinate2D(latitude: 54.1117, longitude: 18.7949),
                CLLocationCoordinate2D(latitude: 54.1122, longitude: 18.7969),
                CLLocationCoordinate2D(latitude: 54.1109, longitude: 18.7977),
                CLLocationCoordinate2D(latitude: 54.1105, longitude: 18.7957)
            ]
        ),
        PolygonBuilding(
            id: 3,
            name: "Hala B3",
            corners: [
                CLLocationCoordinate2D(latitude: 54.1111, longitude: 18.7930),
                CLLocationCoordinate2D(latitude: 54.1115, longitude: 18.7945),
                CLLocationCoordinate2D(latitude: 54.1104, longitude: 18.7953),
                CLLocationCoordinate2D(latitude: 54.1101, longitude: 18.7938)
            ]
        ),
        PolygonBuilding(
            id: 4,
            name: "Hala B4",
            corners: [
                CLLocationCoordinate2D(latitude: 54.1108, longitude: 18.7911),
                CLLocationCoordinate2D(latitude: 54.1110, longitude: 18.7921),
                CLLocationCoordinate2D(latitude: 54.1099, longitude: 18.7930),
                CLLocationCoordinate2D(latitude: 54.1097, longitude: 18.7918)
            ]
        ),
        PolygonBuilding(
            id: 5,
            name: "Hala B5",
            corners: [
                CLLocationCoordinate2D(latitude: 54.1103, longitude: 18.7891),
                CLLocationCoordinate2D(latitude: 54.1107, longitude: 18.7910),
                CLLocationCoordinate2D(latitude: 54.1109, longitude: 18.7918),
                CLLocationCoordinate2D(latitude: 54.1092, longitude: 18.7898)
            ]
        ),
    ]
    
    // MARK: - Stan UI
    @State private var title: String = ""
    @State private var description: String = ""
    @State private var deviceIdText: String = ""
    @State private var selectedStatus: TicketStatus = .nowe
    @State private var devices: [Device] = []
    @State private var selectedDeviceId: Int? = nil
    
    @State private var assignedBuilding: Building? = nil
    @State private var showManualSelection = false
    
    @State private var showConfirmation = false
    @State private var navigateToList = false
    
    @State private var isSending = false
    @State private var validationError: String?
    
    func fetchDevices() {
        guard let url = URL(string: "http://localhost:8080/devices") else {
            print("Niepoprawny URL urządzeń")
            return
        }

        URLSession.shared.dataTask(with: url) { data, response, error in
            if let error = error {
                print("Błąd pobierania urządzeń: \(error)")
                return
            }

            guard let data = data else {
                print("Brak danych z backendu")
                return
            }

            do {
                let decoded = try JSONDecoder().decode([Device].self, from: data)
                DispatchQueue.main.async {
                    self.devices = decoded
                    print("Pobrane urządzenia: \(decoded)")
                }
            } catch {
                print("Błąd dekodowania urządzeń: \(error)")
            }
        }.resume()
    }
    
    func isPointInsidePolygon(point: CLLocationCoordinate2D, polygon: [CLLocationCoordinate2D]) -> Bool {
        guard polygon.count >= 3 else { return false }
        
        let px = point.longitude
        let py = point.latitude
        var inside = false
        var j = polygon.count - 1
        
        for i in 0..<polygon.count {
            let xi = polygon[i].longitude
            let yi = polygon[i].latitude
            let xj = polygon[j].longitude
            let yj = polygon[j].latitude
            
            let intersect = ((yi > py) != (yj > py)) &&
            (px < (xj - xi) * (py - yi) / (yj - yi + 0.0000001) + xi)
            if intersect { inside.toggle() }
            j = i
        }
        return inside
    }
    
    func matchedBuilding(for location: CLLocationCoordinate2D) -> PolygonBuilding? {
        polygonBuildings.first { isPointInsidePolygon(point: location, polygon: $0.corners) }
    }
    
    // MARK: - Wysyłka
    func sendTicketToBackend(ticket: Ticket) {
        guard let url = URL(string: "http://localhost:8080/ticket") else {
            print("Błąd: Niepoprawny URL")
            return
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        
        let encoder = JSONEncoder()
        encoder.dateEncodingStrategy = .iso8601
        
        do {
            let jsonData = try encoder.encode(ticket)
#if DEBUG
            if let jsonString = String(data: jsonData, encoding: .utf8) {
                print("Wysyłany JSON:\n\(jsonString)")
            }
#endif
            request.httpBody = jsonData
        } catch {
            print("Błąd kodowania JSON: \(error)")
            return
        }
        
        isSending = true
        URLSession.shared.dataTask(with: request) { data, response, error in
            DispatchQueue.main.async {
                isSending = false
            }
            
            if let error = error {
                print("Błąd wysyłania zgłoszenia: \(error)")
                return
            }
            
            guard let httpResponse = response as? HTTPURLResponse else {
                print("Brak poprawnej odpowiedzi HTTP")
                return
            }
            
            guard (200...299).contains(httpResponse.statusCode) else {
                print("Błąd HTTP: \(httpResponse.statusCode)")
                return
            }
            
            DispatchQueue.main.async {
                showConfirmation = true
            }
            print("Zgłoszenie zostało pomyślnie wysłane na backend.")
        }.resume()
    }
    
    // MARK: - UI
    var body: some View {
        VStack(spacing: 16) {
            // Tytuł
            TextField("Tytuł zgłoszenia", text: $title)
                .textFieldStyle(.roundedBorder)
                .padding(.horizontal)
            
            // Opis
            TextField("Opis zgłoszenia", text: $description, axis: .vertical)
                .textFieldStyle(.roundedBorder)
                .padding(.horizontal)
        
            // Urządzenie
            Section(header: Text("Urządzenie")
                .font(.footnote)
                .foregroundColor(.gray)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)
            ) {
                Picker("Wybierz urządzenie", selection: $selectedDeviceId) {
                    Text("Wybierz urządzenie").tag(nil as Int?)
                    
                    ForEach(devices, id: \.id) { device in
                        Text(device.name).tag(device.id as Int?)
                    }
                }
                .pickerStyle(.menu)
                .padding(.horizontal)
            }
            
            // Status
            Picker("Status awarii", selection: $selectedStatus) {
                ForEach(TicketStatus.allCases) { status in
                    Text(status.rawValue).tag(status)
                }
            }
            .pickerStyle(.segmented)
            .padding(.horizontal)
            
            // Lokalizacja
            if let location = locationManager.location {
                VStack(alignment: .leading, spacing: 6) {
                    Text("Lokalizacja zgłoszenia:")
                        .font(.subheadline)
                        .foregroundColor(.gray)
                    
                    Text("N/S: \(location.latitude)")
                    Text("W/E: \(location.longitude)")
                    
                    if let building = assignedBuilding {
                        Text("Przypisany budynek: \(building.name)")
                            .font(.headline)
                            .foregroundColor(.blue)
                    } else if showManualSelection {
                        Text("Nie znaleziono budynku. Wybierz ręcznie.")
                            .foregroundColor(.red)
                    }
                    
                    Button("Otwórz w mapach") {
                        let lat = location.latitude
                        let lon = location.longitude
                        if let url = URL(string: "http://maps.apple.com/?ll=\(lat),\(lon)") {
                            UIApplication.shared.open(url)
                        }
                    }
                    .font(.footnote)
                }
                .padding(.horizontal)
            }
            
            if let building = assignedBuilding {
                Text("Wybrany budynek: \(building.name)")
                    .font(.subheadline)
                    .foregroundColor(.blue)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal)
            }
            
            // Komunikat walidacji
            if let validationError {
                Text(validationError)
                    .font(.footnote)
                    .foregroundColor(.red)
                    .padding(.horizontal)
            }
            
            // Submit
            Button(action: {
                validationError = nil
                
                // Prosta walidacja
                guard !appState.currentUser.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
                    validationError = "Brak zalogowanego użytkownika."
                    return
                }
                guard !title.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
                    validationError = "Podaj tytuł zgłoszenia."
                    return
                }
                
                guard let selectedDeviceId else {
                    validationError = "Wybierz urządzenie."
                    return
                }
                
                let newTicket = Ticket(
                    id: Int.random(in: 1000...9999),
                    title: title,
                    description: description,
                    status: selectedStatus.rawValue,
                    user: UserDTO(id: 0, userName: appState.currentUser),
                    timestamp: Date(),
                    latitude: locationManager.location?.latitude,
                    longitude: locationManager.location?.longitude,
                    building: assignedBuilding?.name,
                    technician: nil,
                    deviceId: Int64(selectedDeviceId)
                )
                
                sendTicketToBackend(ticket: newTicket)
#if DEBUG
                print("Wysłano: \(title) - \(description), status: \(selectedStatus.rawValue)")
#endif
            }) {
                HStack(spacing: 8) {
                    if isSending {
                        ProgressView()
                    }
                    Text(isSending ? "Wysyłanie..." : "Wyślij zgłoszenie")
                }
                .frame(maxWidth: .infinity)
                .padding()
                .background(isSending ? Color.gray : Color.purple)
                .foregroundColor(.white)
                .cornerRadius(24)
                .padding(.horizontal)
            }
            .disabled(isSending)
            
            // Ręczny wybór budynku, jeśli nie dopasowano
            if showManualSelection {
                VStack(alignment: .leading, spacing: 6) {
                    Text("Wybierz budynek:")
                        .font(.subheadline)
                        .foregroundColor(.gray)
                        .padding(.horizontal)
                    
                    Picker("Wybierz budynek", selection: $assignedBuilding) {
                        ForEach(polygonBuildings.map {
                            Building(id: $0.id,
                                     name: $0.name,
                                     latitude: $0.corners[0].latitude,
                                     longitude: $0.corners[0].longitude)
                        }) { building in
                            Text(building.name).tag(Optional(building))
                        }
                    }
                    .pickerStyle(.menu)
                    .padding(.horizontal)
                    .background(Color(.systemGray6))
                    .cornerRadius(8)
                }
            }
            
            Spacer(minLength: 8)
        }
        .padding(.top)
        .navigationTitle("Zgłoś awarię")
        .onReceive(locationManager.$location) { newLocation in
            guard let location = newLocation else { return }
            
            if let match = matchedBuilding(for: location) {
                assignedBuilding = Building(
                    id: match.id,
                    name: match.name,
                    latitude: location.latitude,
                    longitude: location.longitude
                )
                showManualSelection = false
            } else {
                showManualSelection = true
            }
        }
        .alert(isPresented: $showConfirmation) {
            Alert(
                title: Text("Wysłano zgłoszenie"),
                message: Text("Twoje zgłoszenie zostało wysłane."),
                dismissButton: .default(Text("OK")) {
                    navigateToList = true
                }
            )
        }
        .background(
            NavigationLink(destination: TicketListView(), isActive: $navigateToList) {
                EmptyView()
            }
        )
        .onAppear {
            fetchDevices()
        }
    }
}
