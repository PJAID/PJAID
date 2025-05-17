//
//  ReportFailureView.swift
//  pjaidMobileiOS
//
//  Created by Adrian Goik on 28/04/2025.
//


import SwiftUI

struct ReportFailureView: View {
    @EnvironmentObject var appState: AppState
    @StateObject private var locationManager = LocationManager()
    @State private var title: String = ""
    @State private var description: String = ""
    @State private var showConfirmation = false
    @State private var navigateToList = false
    
    
    var body: some View {
        VStack(spacing: 20) {
            TextField("Tytuł zgłoszenia", text: $title)
                .textFieldStyle(.roundedBorder)
                .padding(.horizontal)
            
            TextField("Opis zgłoszenia", text: $description)
                .textFieldStyle(.roundedBorder)
                .padding(.horizontal)
            
            if let location = locationManager.location {
                VStack(alignment: .leading) {
                    Text("Lokalizacja zgłoszenia:")
                        .font(.subheadline)
                        .foregroundColor(.gray)

                    Text("N/S: \(location.latitude)")
                    Text("W/E: \(location.longitude)")

                    Button("Otwórz w Google Maps") {
                        if let url = URL(string: "https://www.google.com/maps?q=\(location.latitude),\(location.longitude)") {
                            UIApplication.shared.open(url)
                        }
                    }
                    .font(.footnote)
                }
                .padding()
            }

            
            Button(action: {
                            let newTicket = Ticket(
                                id: Int.random(in: 1000...9999),
                                title: title,
                                description: description,
                                status: "Nowe",
                                user: appState.currentUser,
                                timestamp: Date(),
                                latitude: locationManager.location?.latitude,
                                longitude: locationManager.location?.longitude
                            )
                            appState.userTickets.insert(newTicket, at: 0)
                            showConfirmation = true
                        }) {
                            Text("Wyślij zgłoszenie")
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color.purple)
                                .foregroundColor(.white)
                                .cornerRadius(24)
                                .padding(.horizontal)
                        }

                        Spacer()
        }
        .padding(.top)
        .navigationTitle("Zgłoś awarię")
        .alert(isPresented: $showConfirmation) {
            Alert(
                title: Text("Wysłano zgłoszenie"),
                message: Text("Twoje zgłoszenie zostało wysłane."),
                dismissButton: .default(Text("OK")) {
                    navigateToList = true
                }
            )
        }
        Button("OK"){
            navigateToList = true
        }
        .background(
            NavigationLink(destination: TicketListView(), isActive: $navigateToList,
                           label: { EmptyView() }) 
        )
    }
}
