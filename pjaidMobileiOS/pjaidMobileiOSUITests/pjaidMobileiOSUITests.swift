//
//  pjaidMobileiOSUITests.swift
//  pjaidMobileiOSUITests
//
//  Created by Adrian Goik on 19/04/2025.
//

import XCTest

final class pjaidMobileiOSUITests: XCTestCase {
    func testHappyPath_ListAndDetail() {
        let app = XCUIApplication()
        app.launchEnvironment["UITEST_FAKE_QR"] = "1"
        app.launch()

        // Login ekran
        let email = app.textFields["Username or email"]
        XCTAssertTrue(email.waitForExistence(timeout: 5))
        email.tap(); email.typeText("admin")

        let password = app.secureTextFields["Password"]
        password.tap(); password.typeText("secret")

        app.buttons["Sign in"].tap()

        // Menu
        let scanTile = app.buttons["Skanuj kod QR"]
        XCTAssertTrue(scanTile.waitForExistence(timeout: 5))

        app.buttons["Lista zgłoszeń"].tap()

        // Lista
        let nav = app.navigationBars["Zgłoszenia"]
        XCTAssertTrue(nav.waitForExistence(timeout: 5))

    }
}
