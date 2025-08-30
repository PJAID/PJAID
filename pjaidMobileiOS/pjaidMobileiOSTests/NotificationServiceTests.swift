//
//  NotificationServiceTests.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 20/08/2025.
//

import XCTest
@testable import pjaidMobileiOS

struct MockNotificationService: NotificationServicing {
    var granted = true
    func requestAuth() async throws -> Bool { granted }
    func scheduleTest() {}
}

final class NotificationServiceTests: XCTestCase {
    func testAuthGranted() async throws {
        let mock = MockNotificationService(granted: true)
        let ok = try await mock.requestAuth()
        XCTAssertTrue(ok)
    }
}
