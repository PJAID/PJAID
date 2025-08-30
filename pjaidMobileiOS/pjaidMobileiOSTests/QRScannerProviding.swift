//
//  QRScannerProviding.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 20/08/2025.
//

import Foundation
import SwiftUI
import CodeScanner

protocol QRScannerProviding {
    associatedtype Body: View
    @ViewBuilder func makeScanner(completion: @escaping (Result<ScanResult, ScanError>) -> Void) -> Body
}

struct RealQRScannerProvider: QRScannerProviding {
    func makeScanner(completion: @escaping (Result<ScanResult, ScanError>) -> Void) -> some View {
        CodeScannerView(codeTypes: [.qr], completion: completion)
    }
}

// W QRScannerScreen wstrzykujemy provider (z domyślnym realnym):
struct QRScannerScreen<Provider: QRScannerProviding>: View {
    @State private var isPresentingScanner = false
    @State private var scannedCode: String?
    private let provider: Provider

    init(provider: Provider = RealQRScannerProvider() as! Provider) { self.provider = provider }

    var body: some View {
        NavigationStack {
            VStack(spacing: 20) {
                Button("Skanuj QR Kod") { isPresentingScanner = true }
                    .padding().background(Color.purple).foregroundColor(.white).cornerRadius(12)

                if let code = scannedCode {
                    Text("Zeskanowany kod:").font(.headline)
                    Text(code).font(.subheadline).foregroundColor(.gray).padding()
                }
            }
            .navigationTitle("Skaner QR")
            .sheet(isPresented: $isPresentingScanner) {
                provider.makeScanner { result in handleScan(result: result) }
            }
        }
    }

    func handleScan(result: Result<ScanResult, ScanError>) {
        isPresentingScanner = false
        switch result {
        case .success(let r): scannedCode = r.string
        case .failure(let e): print("Błąd skanowania: \(e.localizedDescription)")
        }
    }
}
