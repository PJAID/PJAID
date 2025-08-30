//
//  NotificationService.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 20/08/2025.
//

import UserNotifications

protocol NotificationServicing {
    func requestAuth() async throws -> Bool
    func scheduleTest()
}
struct NotificationService: NotificationServicing {
    func requestAuth() async throws -> Bool {
        try await withCheckedThrowingContinuation { cont in
            UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
                if let e = error { cont.resume(throwing: e) } else { cont.resume(returning: granted) }
            }
        }
    }
    func scheduleTest() {
        // przenieś tu sendTestNotification()
    }
}
