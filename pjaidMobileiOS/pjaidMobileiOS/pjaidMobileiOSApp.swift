//
//  pjaidMobileiOSApp.swift
//  pjaidMobileiOS
//
//  Created by Adrian Goik on 19/04/2025.
//

import SwiftUI
import UserNotifications

@main
struct pjaidMobileiOSApp: App {
    @StateObject private var appState = AppState()

    init() {
        requestNotificationPermission()
    }

    var body: some Scene {
        WindowGroup {
            if appState.isLoggedIn {
                MenuView()
                    .environmentObject(appState)
            } else {
                LoginView()
                    .environmentObject(appState)
            }
        }
    }

    func requestNotificationPermission() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
            if let error = error {
                print("Błąd przy żądaniu powiadomień: \(error.localizedDescription)")
            } else {
                print("Uprawnienia powiadomień przyznane: \(granted)")
            }
        }
    }

    func sendTestNotification() {
        let content = UNMutableNotificationContent()
        content.body = "To jest testowe powiadomienie"
        content.sound = .default

        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 5, repeats: false)
        let request = UNNotificationRequest(identifier: UUID().uuidString, content: content, trigger: trigger)

        UNUserNotificationCenter.current().add(request) { error in
            if let error = error {
                print("Błąd przy dodaniu powiadomienia: \(error.localizedDescription)")
            } else {
                print("Powiadomienie testowe zostało zaplanowane.")
            }
        }
    }
}
