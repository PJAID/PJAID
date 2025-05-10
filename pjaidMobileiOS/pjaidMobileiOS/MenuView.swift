import SwiftUI
import CodeScanner
import UserNotifications



struct MenuView: View {
    
    @EnvironmentObject var appState: AppState
    
    var body: some View {
        NavigationStack {
            ZStack {
                Image("background")
                    .resizable()
                    .scaledToFill()
                    .clipped()
                    .ignoresSafeArea()

                VStack(spacing: 20) {
                    Image("logo")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 250)
                        .padding(.top)

                    Text("Zalogowano jako: \(appState.currentUser)")
                        .foregroundColor(.white)
                        .font(.subheadline)
                        .padding(.bottom)

                    NavigationLink(destination: QRScannerScreen()) {
                        VStack {
                            Image(systemName: "qrcode.viewfinder")
                                .font(.largeTitle)
                                .padding()
                            Text("Skanuj kod QR")
                                .font(.headline)
                        }
                        .primaryButtonStyle(color: .blue)
                    }

                    NavigationLink(destination: ReportFailureView()) {
                        VStack {
                            Image(systemName: "exclamationmark.triangle.fill")
                                .font(.largeTitle)
                                .padding()
                            Text("Zgłoś awarię")
                                .font(.headline)
                        }
                        .primaryButtonStyle(color: .purple)
                    }
                        NavigationLink(destination: TicketListView()) {
                            VStack {
                                Image(systemName: "list.bullet")
                                    .font(.largeTitle)
                                    .padding()
                                Text("Lista zgłoszeń")
                                    .font(.headline)
                            }
                            .primaryButtonStyle(color: .indigo)
                        }

                    Spacer()
                }
                .padding()
            }
        }
    }
}

extension View {
    func primaryButtonStyle(color: Color) -> some View {
        self
            .frame(maxWidth: .infinity, minHeight: 120)
            .background(color.opacity(0.3))
            .foregroundColor(.white)
            .cornerRadius(16)
            .padding(.horizontal)
    }
}
