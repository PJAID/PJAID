//
//  QRScanner.swift
//  pjaidMobileiOS
//

import SwiftUI
import CodeScanner

struct QRScannerScreen: View {
    @State private var isPresentingScanner = false
    @State private var scannedCode: String?

    // NOWE: przełącznik testowy (domyślnie false, czyli realny skaner)
    let useFake: Bool

    init(useFake: Bool = false) {
        self.useFake = useFake
    }

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
                if useFake {
                    // FAKE – tylko do testów UI
                    FakeScannerView { result in
                        switch result {
                        case .success(let code):
                            scannedCode = code
                            print("FAKE wynik: \(code)")
                        case .failure(let error):
                            print("Błąd fake: \(error.localizedDescription)")
                        }
                        isPresentingScanner = false
                    }
                } else {
                    // REALNY skaner – normalna praca aplikacji
                    CodeScannerView(
                        codeTypes: [.qr],
                        completion: handleScan
                    )
                }
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

// Prosty „fejkowy” skaner do testów – zwraca z góry ustalony kod
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
