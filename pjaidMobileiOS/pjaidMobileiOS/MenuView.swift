import SwiftUI
import CodeScanner

struct MenuView: View {
    @State private var isShowingScanner = false
    @State private var scannedCode: String?
    
    var body: some View {
        NavigationStack {
            VStack(spacing: 20) {
                
                // Kafelek do przejścia do zgłoszenia awarii
                NavigationLink(destination: ReportFailureView()) {
                    VStack {
                        Image(systemName: "exclamationmark.triangle.fill")
                            .font(.largeTitle)
                            .padding()
                        Text("Zgłoś awarię")
                            .font(.headline)
                    }
                    .frame(maxWidth: .infinity, minHeight: 150)
                    .background(Color.purple.opacity(0.2))
                    .cornerRadius(16)
                    .padding(.horizontal)
                }
                
                // Kafelek do skanowania QR kodu
                Button {
                    isShowingScanner = true
                } label: {
                    VStack {
                        Image(systemName: "qrcode.viewfinder")
                            .font(.largeTitle)
                            .padding()
                        Text("Skanuj QR kod")
                            .font(.headline)
                    }
                    .frame(maxWidth: .infinity, minHeight: 150)
                    .background(Color.blue.opacity(0.2))
                    .cornerRadius(16)
                    .padding(.horizontal)
                }
                
                // Wynik skanowania
                if let code = scannedCode {
                    VStack(spacing: 10) {
                        Text("Zeskanowany kod:")
                            .font(.headline)
                        Text(code)
                            .font(.subheadline)
                            .foregroundColor(.gray)
                            .padding(.horizontal)
                    }
                    .padding(.top)
                }
                
                Spacer()
            }
            .navigationTitle("PJAID Menu")
            .sheet(isPresented: $isShowingScanner) {
                CodeScannerView(codeTypes: [.qr], completion: handleScan)
            }
        }
    }
    
    func handleScan(result: Result<ScanResult, ScanError>) {
        isShowingScanner = false
        
        switch result {
        case .success(let scanResult):
            scannedCode = scanResult.string
            print("Zeskanowany kod: \(scanResult.string)")
        case .failure(let error):
            print("Błąd skanowania: \(error.localizedDescription)")
        }
    }
}
