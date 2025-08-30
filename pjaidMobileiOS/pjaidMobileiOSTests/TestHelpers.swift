//
//  TestHelpers.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 21/08/2025.
//

import Foundation
@testable import pjaidMobileiOS

/// Minimalna implementacja loginu używana w testach.
/// Korzysta z wstrzykniętego HTTPClient (StubHTTPClient), więc nie robi prawdziwego requestu.
func performLogin(http: HTTPClient, username: String, password: String) async throws -> (Int, Data) {
    let url = URL(string: "http://localhost:8080/api/auth/login")!
    var req = URLRequest(url: url)
    req.httpMethod = "POST"
    req.setValue("application/json", forHTTPHeaderField: "Content-Type")

    let body: [String: Any] = [
        "username": username,
        "password": password
    ]
    req.httpBody = try JSONSerialization.data(withJSONObject: body)

    let (data, resp) = try await http.data(for: req)
    let status = (resp as? HTTPURLResponse)?.statusCode ?? -1
    return (status, data)
}
