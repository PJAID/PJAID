//
//  TicketListView.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 20/04/2025.
//

import SwiftUI

struct Ticket: Identifiable, Codable {
    let id: Int //nie lepiej zmienić na UUID?
    let title: String
    let description: String
    let status: String
    var user: String? = nil // lokalne zgłoszenia
    var timestamp: Date? = nil

    enum CodingKeys: String, CodingKey {
        case id, title, description, status
    }
}

struct TicketListView: View {
    @EnvironmentObject var appState: AppState
    @State private var apiTickets: [Ticket] = []
    //@State private var tickets: [Ticket] = []
    @State private var isLoading = true

    var body: some View {
        NavigationView {
            List(appState.userTickets + apiTickets) { ticket in
                NavigationLink(destination: TicketDetailView(ticket: ticket)) {
                    VStack(alignment: .leading, spacing: 4) {
                        Text(ticket.title)
                            .font(.headline)
                        if ticket.user == appState.currentUser {
                            Text("Twoje zgłoszenie")
                                .font(.caption)
                                .foregroundColor(.purple)
                        }
                    }
                    .padding(5)
                    .background(ticket.user == appState.currentUser ? Color.purple.opacity(0.1) : Color.clear)
                    .cornerRadius(8)
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

    func fetchTickets(completion: @escaping ([Ticket]) -> Void) {
        let url = URL(string: "http://localhost:8080/ticket")! // lub IP Maca

        URLSession.shared.dataTask(with: url) { data, response, error in
            guard let data = data else { return }

            do {
                let decoder = JSONDecoder()
                decoder.dateDecodingStrategy = .iso8601
                let tickets = try decoder.decode([Ticket].self, from: data)
                DispatchQueue.main.async {
                    completion(tickets)
                }
            } catch {
                print("Decode error: \(error)")
            }
        }.resume()
    }
}
