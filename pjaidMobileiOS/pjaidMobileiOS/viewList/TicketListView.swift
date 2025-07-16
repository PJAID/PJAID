//
//  TicketListView.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 20/04/2025.
//
import SwiftUI

struct TicketListView: View {
    @EnvironmentObject var appState: AppState
    @State private var apiTickets: [Ticket] = []
    @State private var isLoading = true
    @State private var selectedTab = 0 // 0 = Wszystkie, 1 = Moje zgłoszenia

    var body: some View {
        NavigationView {
            VStack {
                Picker("Widok zgłoszeń", selection: $selectedTab) {
                    Text("Wszystkie").tag(0)
                    Text("Moje zgłoszenia").tag(1)
                }
                .pickerStyle(SegmentedPickerStyle())
                .padding()

                if isLoading {
                    ProgressView("Ładowanie zgłoszeń...")
                        .padding()
                } else {
                    List(filteredTickets()) { ticket in
                        NavigationLink(destination: TicketDetailView(ticket: ticket)) {
                            TicketRowView(ticket: ticket, currentUser: appState.currentUser)
                        }
                    }
                }
            }
            .navigationTitle("Zgłoszenia")
            .onAppear {
                fetchTickets { loadedTickets in
                    self.apiTickets = loadedTickets
                    self.isLoading = false
                }
            }
        }
    }

    func filteredTickets() -> [Ticket] {
        switch selectedTab {
        case 0:
            return apiTickets
        case 1:
            return apiTickets.filter {
                $0.technician?.userName?.lowercased() == appState.currentUser
            }
        default:
            return []
        }
    }

    func fetchTickets(completion: @escaping ([Ticket]) -> Void) {
        guard let url = URL(string: "http://localhost:8080/ticket") else {
            print("Niepoprawny URL")
            return
        }

        URLSession.shared.dataTask(with: url) { data, response, error in
            guard let data = data else {
                print("Brak danych z serwera")
                return
            }

            do {
                let decoder = JSONDecoder()
                decoder.dateDecodingStrategy = .iso8601
                let tickets = try decoder.decode([Ticket].self, from: data)
                DispatchQueue.main.async {
                    completion(tickets)
                }
            } catch {
                print("Błąd dekodowania: \(error)")
            }
        }.resume()
    }
}
