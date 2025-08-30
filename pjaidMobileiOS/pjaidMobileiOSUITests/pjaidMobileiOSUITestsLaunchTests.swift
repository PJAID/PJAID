//
//  pjaidMobileiOSUITestsLaunchTests.swift
//  pjaidMobileiOSUITests
//
//  Created by Adrian Goik on 19/04/2025.
//

import XCTest

final class pjaidMobileiOSUITestsLaunchTests: XCTestCase {

    override class var runsForEachTargetApplicationUIConfiguration: Bool {
        true
    }

    override func setUpWithError() throws {
        continueAfterFailure = false
    }

    @MainActor
    func testLaunch() throws {
        let app = XCUIApplication()
        app.launch()


        let attachment = XCTAttachment(screenshot: app.screenshot())
        attachment.name = "Launch Screen"
        attachment.lifetime = .keepAlways
        add(attachment)
    }
}
