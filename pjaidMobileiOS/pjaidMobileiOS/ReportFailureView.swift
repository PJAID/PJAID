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
    let corners: [CLLocationCoordinate2D] // dokładnie 4 punkty, kolejność: zgodnie z ruchem wskazówek
}


struct ReportFailureView: View {
    @EnvironmentObject var appState: AppState
    @StateObject private var locationManager = LocationManager()
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
    @State private var title: String = ""
    @State private var description: String = ""
    @State private var showConfirmation = false
    @State private var navigateToList = false
    @State private var selectedStatus: TicketStatus = .nowe
    @State private var assignedBuilding: Building? = nil
    @State private var showManualSelection = false

    func isPointInsidePolygon(point: CLLocationCoordinate2D, polygon: [CLLocationCoordinate2D]) -> Bool {
        var inside = false
        var j = polygon.count - 1

        for i in 0..<polygon.count {
            let xi = polygon[i].latitude
            let yi = polygon[i].longitude
            let xj = polygon[j].latitude
            let yj = polygon[j].longitude

            if ((yi > point.longitude) != (yj > point.longitude)) {
                let x = (xj - xi) * (point.longitude - yi) / (yj - yi) + xi
                if point.latitude < x {
                    inside.toggle()
                }
            }
            j = i
        }

        return inside
    }

    func matchedBuilding(for location: CLLocationCoordinate2D) -> PolygonBuilding? {
        for building in polygonBuildings {
            if isPointInsidePolygon(point: location, polygon: building.corners) {
                return building
            }
        }
        return nil
    }
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
            
            if let jsonString = String(data: jsonData, encoding: .utf8) {
                print("Wysyłany JSON:\n\(jsonString)")
            }

            request.httpBody = jsonData
        } catch {
            print("Błąd kodowania JSON: \(error)")
            return
        }

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Błąd wysyłania zgłoszenia: \(error)")
                return
            }

            guard let httpResponse = response as? HTTPURLResponse,
                  (200...299).contains(httpResponse.statusCode) else {
                print("Błąd HTTP: \(response.debugDescription)")
                return
            }

            DispatchQueue.main.async {
                showConfirmation = true
            }

            print("Zgłoszenie zostało pomyślnie wysłane na backend.")
        }.resume()
    }
    
    var body: some View {
        VStack(spacing: 20) {
            TextField("Tytuł zgłoszenia", text: $title)
                .textFieldStyle(.roundedBorder)
                .padding(.horizontal)
            
            TextField("Opis zgłoszenia", text: $description)
                .textFieldStyle(.roundedBorder)
                .padding(.horizontal)
            
            Picker("Status awarii", selection: $selectedStatus) {
                ForEach(TicketStatus.allCases) { status in
                    Text(status.rawValue).tag(status)
                }
            }
            .pickerStyle(.segmented)
            .padding(.horizontal)
          
               if let location = locationManager.location {
                VStack(alignment: .leading) {
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
                 
                .padding()
               }
            if let building = assignedBuilding {
                Text("Wybrany budynek: \(building.name)")
                    .font(.subheadline)
                    .foregroundColor(.blue)
            }


            Button(action: {
                print("Aktualny użytkownik: '\(appState.currentUser)'")
                guard !appState.currentUser.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
                    print("❌ Brak użytkownika – nie można wysłać zgłoszenia")
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
                    technician: nil
                )

                sendTicketToBackend(ticket: newTicket)
                print("Wysłano: \(title) - \(description), status: \(selectedStatus.rawValue)")

            }) {
                Text("Wyślij zgłoszenie")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.purple)
                    .foregroundColor(.white)
                    .cornerRadius(24)
                    .padding(.horizontal)
            }
            if showManualSelection {
                VStack(alignment: .leading, spacing: 6) {
                    Text("Wybierz budynek:")
                        .font(.subheadline)
                        .foregroundColor(.gray)
                        .padding(.horizontal)

                    Picker("Wybierz budynek", selection: $assignedBuilding) {
                        ForEach(polygonBuildings.map {
                            Building(id: $0.id, name: $0.name, latitude: $0.corners[0].latitude, longitude: $0.corners[0].longitude)
                        }) { building in
                            Text(building.name).tag(Optional(building))
                        }
                    }
                    .pickerStyle(MenuPickerStyle())
                    .padding(.horizontal)
                    .background(Color(.systemGray6))
                    .cornerRadius(8)
                }
            }
            Spacer()
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
    }
}
