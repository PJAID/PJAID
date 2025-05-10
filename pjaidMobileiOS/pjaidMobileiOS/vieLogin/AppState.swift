//
//  AppState.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 07/05/2025.
//  Modified by Adrian Goik on 8/05/2025

import Foundation
import Combine

class AppState: ObservableObject {
    @Published var isLoggedIn = false
    @Published var currentUser: String = ""
    @Published var userTickets: [Ticket] = []
}
