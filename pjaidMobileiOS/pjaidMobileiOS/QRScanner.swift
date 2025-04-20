//
//  QRScanner.swift
//  pjaidMobileiOS
//
//  Created by Adrian Goik on 20/04/2025.
//
import SwiftUI
import CodeScanner

struct QRScannerScreen: View {
    @State private var isPresentingScanner = false
    @State private var scannedCode: String?

    var body: some View {
        NavigationStack {
            VStack(spacing: 20) {
                Button("Skanuj QR Kod") {
                    isPresentingScanner = true
                }
                .padding()
                .background(Color.purple)
                .foregroundColor(.white)
                .cornerRadius(12)

                if let code = scannedCode {
                    Text("Zeskanowany kod:")
                        .font(.headline)
                    Text(code)
                        .font(.subheadline)
                        .foregroundColor(.gray)
                        .padding()
                }
            }
            .navigationTitle("Skaner QR")
            .sheet(isPresented: $isPresentingScanner) {
                CodeScannerView(
                    codeTypes: [.qr],
                    completion: handleScan
                )
            }
        }
    }

    func handleScan(result: Result<ScanResult, ScanError>) {
        isPresentingScanner = false

        switch result {
        case .success(let scanResult):
            scannedCode = scanResult.string
            print("Wynik skanowania: \(scanResult.string)")
        case .failure(let error):
            print("Błąd skanowania: \(error.localizedDescription)")
        }
    }
}

