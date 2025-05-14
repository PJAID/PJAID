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
} //try to implement location later on in SPRINT 8

struct User{
    let name: String
    let email: String
}
