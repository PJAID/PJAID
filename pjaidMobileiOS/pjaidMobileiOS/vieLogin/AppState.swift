//
//  AppState.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 07/05/2025.
//

import Foundation
import Combine

class AppState: ObservableObject {
    @Published var isLoggedIn = false
    @Published var currentUser: String = ""
}
