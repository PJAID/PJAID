//
//  TicketListView.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 20/04/2025.
//

import SwiftUI

struct Ticket: Identifiable, Codable {
    let id: Int
    let title: String
    let description: String
    let status: String

    enum CodingKeys: String, CodingKey {
        case id, title, description, status
    }
}

struct TicketListView: View {
    @State private var tickets: [Ticket] = []
    @State private var isLoading = true

    var body: some View {
        NavigationView {
            List(tickets) { ticket in
                NavigationLink(destination: TicketDetailView(ticket: ticket)) {
                    Text(ticket.title)
                        .font(.headline)
                }
            }
            .navigationTitle("Zgłoszenia")
            .onAppear {
                fetchTickets { loadedTickets in
                    self.tickets = loadedTickets
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
