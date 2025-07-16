//
//  TicketListView.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 20/04/2025.
//
import SwiftUI

struct TicketRowView: View {
    let ticket: Ticket
    let currentUser: String

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(ticket.title)
                .font(.headline)

            if let building = ticket.building {
                Text("Budynek: \(building)")
                    .font(.caption)
                    .foregroundColor(.gray)
            }

            if ticket.user?.username == currentUser {
                Text("Twoje zgłoszenie")
                    .font(.caption2)
                    .foregroundColor(.purple)
            }

            Text("Status: \(ticket.status)")
                .font(.caption2)
                .foregroundColor(.blue)
        }
        .padding(8)
        .background(ticket.user?.username == currentUser ? Color.purple.opacity(0.1) : Color(.systemBackground))
        .cornerRadius(10)
        .shadow(color: Color.black.opacity(0.05), radius: 2, x: 0, y: 2)
    }
}
