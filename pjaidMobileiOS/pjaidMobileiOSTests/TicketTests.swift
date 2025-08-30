//
//  TicketTests.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 20/08/2025.
//

import XCTest
@testable import pjaidMobileiOS

final class TicketTests: XCTestCase {
    func testTicketDecoding() throws {
        let json = """
        [{
            "id": 1,
            "title": "Awaria drukarki",
            "description": "Brak tuszu",
            "status": "OPEN",
            "user": { "id": 7, "userName": "jan" },
            "latitude": 52.23, "longitude": 21.01,
            "building": "A1",
            "technician": { "id": 2, "userName": "adrian" },
            "deviceId": 1001
        }]
        """.data(using: .utf8)!

        let dec = JSONDecoder()
        dec.dateDecodingStrategy = .iso8601
        let tickets = try dec.decode([Ticket].self, from: json)
        XCTAssertEqual(tickets.count, 1)
        XCTAssertEqual(tickets.first?.title, "Awaria drukarki")
        XCTAssertEqual(tickets.first?.technician?.userName, "adrian")
    }

    func testMyTicketsFilter() {
        let app = AppState()
        app.currentUser = "adrian" // w Twoim kodzie porównujesz do technika .lowercased()

        let all: [Ticket] = [
            .init(id: 1, title: "T1", description: "", status: "OPEN",
                  user: nil, timestamp: nil, latitude: nil, longitude: nil,
                  building: nil, technician: .init(id: 1, userName: "adrian"), deviceId: nil),
            .init(id: 2, title: "T2", description: "", status: "OPEN",
                  user: nil, timestamp: nil, latitude: nil, longitude: nil,
                  building: nil, technician: .init(id: 2, userName: "ola"), deviceId: nil)
        ]

        let mine = all.filter { $0.technician?.userName?.lowercased() == app.currentUser }
        XCTAssertEqual(mine.map(\.id), [1])
    }
}
