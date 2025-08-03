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
    let id: Int
    let title: String
    let description: String
    var status: String
    var user: UserDTO?
    var timestamp: Date?
    let latitude: Double?
    let longitude: Double?
    let building: String?
    var technician: TechnicianDTO?

    enum CodingKeys: String, CodingKey {
            case id, title, description, status, user, timestamp, latitude, longitude, building, technician
        }
}
struct Building: Identifiable, Hashable {
    let id: Int
    let name: String
    let latitude: Double
    let longitude: Double
}

struct User{
    let name: String
    let email: String
}
struct UserDTO: Codable {
    let id: Int
    let userName: String?
}

struct TechnicianDTO: Codable {
    let id: Int?
    let userName: String?
}
