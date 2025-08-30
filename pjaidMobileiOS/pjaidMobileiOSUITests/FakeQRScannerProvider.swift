//
//  FakeQRScannerProvider.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 20/08/2025.
//

import SwiftUI
import CodeScanner

struct FakeQRScannerProvider: QRScannerProviding {
    let fakeCode: String
    func makeScanner(completion: @escaping (Result<ScanResult, ScanError>) -> Void) -> some View {
        // Zastępujemy sheet prostym widokiem z przyciskiem „Zwróć wynik”
        return VStack {
            Text("FAKE SCANNER")
            Button("Zwróć wynik") {
                completion(.success(ScanResult(string: fakeCode, type: .qr)))
            }
        }.padding()
    }
}

