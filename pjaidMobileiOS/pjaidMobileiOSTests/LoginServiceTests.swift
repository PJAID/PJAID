//
//  LoginServiceTests.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 20/08/2025.
//

import XCTest
@testable import pjaidMobileiOS

final class LoginServiceTests: XCTestCase {
    func testLogin200() async throws {
        let url = URL(string: "http://localhost:8080/api/auth/login")!
        let body = ["username":"admin","token":"abc"]
        let data = try JSONSerialization.data(withJSONObject: body)
        let response = HTTPURLResponse(url: url, statusCode: 200, httpVersion: nil, headerFields: nil)!

        URLProtocolStub.stubs[url] = .init(data: data, response: response, error: nil)

        let http = StubHTTPClient()
        let (code, payload) = try await performLogin(http: http, username: "admin", password: "x")
        XCTAssertEqual(code, 200)

        let decoded = try JSONSerialization.jsonObject(with: payload) as? [String:Any]
        XCTAssertEqual(decoded?["username"] as? String, "admin")
    }
}
