//
//  FakeQRScannerProvider.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 20/08/2025.
//

import SwiftUI
import CodeScanner

struct FakeQRScannerProvider: View {
    let onComplete: (Result<String, ScanError>) -> Void

    var body: some View {
        VStack {
            Text("FAKE SCANNER")
            Button("Zwróć wynik") {
                onComplete(.success("device:123"))
            }
        }
        .padding()
    }
}
