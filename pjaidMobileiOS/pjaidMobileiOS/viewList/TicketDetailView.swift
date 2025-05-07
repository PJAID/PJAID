//
//  TicketDetailView.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 20/04/2025.
//

import SwiftUI

struct TicketDetailView: View {
    let ticket: Ticket

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text("Tytuł: \(ticket.title)")
                .font(.title2)
                .bold()

            Text("Opis: \(ticket.description)")
                .font(.body)

            Text("Status: \(ticket.status)")
                .foregroundColor(.blue)

            Text("Brak daty utworzenia w API")
                .font(.footnote)
                .foregroundColor(.gray)

            Spacer()
        }
        .padding()
        .navigationTitle("Szczegóły")
    }

    private func formattedDate(_ date: Date) -> String {
        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        formatter.timeStyle = .short
        formatter.locale = Locale(identifier: "pl_PL")
        return formatter.string(from: date)
    }
}
