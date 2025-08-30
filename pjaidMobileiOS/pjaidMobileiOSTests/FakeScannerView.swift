//
//  FakeScannerView.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 20/08/2025.
//

import SwiftUI
import CodeScanner

struct FakeScannerView: View {
    let onComplete: (Result<String, ScanError>) -> Void

    var body: some View {
        VStack(spacing: 12) {
            Text("FAKE SCANNER").font(.headline)
            Button("Zwróć wynik") {
                onComplete(.success("device:123"))
            }
        }
        .padding()
    }
}
