//
//  Structs.swift
//  pjaidMobileiOS
//
//  Created by Adrian Goik on 08/05/2025.
//  Structs for quick reference in the project

import Foundation

struct LocalTicket: Identifiable{ //Do ujednolicenia LocalTicket i deklaracja ticketu w TicketListView
    let id = UUID()
    let title: String
    let description: String
    let user: String
    let timestamp: Date
    let latitude: Double?
    let longitude: Double?
} //try to implement location later on in SPRINT 8

struct Ticket: Identifiable, Codable {
    let id: Int //nie lepiej zmienić na UUID?
    let title: String
    let description: String
    let status: String
    var user: String? = nil // lokalne zgłoszenia
    var timestamp: Date? = nil
    let latitude: Double?
    let longitude: Double?

    enum CodingKeys: String, CodingKey {
        case id, title, description, status, latitude, longitude
    }
}


struct User{
    let name: String
    let email: String
}
