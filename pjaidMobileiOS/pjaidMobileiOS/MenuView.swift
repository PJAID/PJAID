import SwiftUI
import CodeScanner

struct MenuView: View {
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
                NavigationLink(destination: QRScannerScreen()) {
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
                
                Spacer()
            }
            .navigationTitle("PJAID Menu")
        }
    }
}
