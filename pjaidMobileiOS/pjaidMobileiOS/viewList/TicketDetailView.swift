//
//  TicketDetailView.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 20/04/2025.
//

import SwiftUI

struct TicketDetailView: View {
    @State var ticket: Ticket
    @Binding var shouldRefresh: Bool
    @EnvironmentObject var appState: AppState
    @State private var showCloseReport = false

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text("Tytuł: \(ticket.title)")
                .font(.title2)
                .bold()

            Text("Opis: \(ticket.description)")
                .font(.body)

            Text("Status: \(ticket.status)")
                .foregroundColor(.blue)

            if let building = ticket.building {
                Text("Budynek: \(building)")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
            }

            Text("Brak daty utworzenia w API")
                .font(.footnote)
                .foregroundColor(.gray)

            if let lat = ticket.latitude,
               let lon = ticket.longitude,
               lat.isFinite,
               lon.isFinite {
                Text("Lokalizacja:")
                    .font(.subheadline)
                    .padding(.top)

                Text("Szerokość: \(lat)")
                Text("Długość: \(lon)")

                Button("Pokaż w Mapach") {
                    if let url = URL(string: "http://maps.apple.com/?ll=\(lat),\(lon)") {
                        UIApplication.shared.open(url)
                    }
                }
                .foregroundColor(.blue)
            }

            if ticket.status.uppercased() != "ZAMKNIETE" {
                Button("Zamknij zgłoszenie") {
                    showCloseReport = true
                }
                .foregroundColor(.red)
                .padding(.top)
            }

            Spacer()
        }
        .padding()
        .navigationTitle("Szczegóły")
        .sheet(isPresented: $showCloseReport) {
            CloseReportView(ticketId: ticket.id) {
                refreshTicket()
                shouldRefresh = true
            }
        }
    }

    private func refreshTicket() {
        guard let url = URL(string: "http://localhost:8080/ticket/\(ticket.id)") else { return }

        URLSession.shared.dataTask(with: url) { data, _, error in
            if let data = data {
                do {
                    let updatedTicket = try JSONDecoder().decode(Ticket.self, from: data)
                    DispatchQueue.main.async {
                        self.ticket = updatedTicket
                    }
                } catch {
                    print("Błąd dekodowania: \(error)")
                }
            } else if let error = error {
                print("Błąd pobierania zgłoszenia: \(error)")
            }
        }.resume()
    }
}
