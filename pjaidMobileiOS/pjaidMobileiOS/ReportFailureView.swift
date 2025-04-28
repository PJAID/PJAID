//
//  ReportFailureView.swift
//  pjaidMobileiOS
//
//  Created by Adrian Goik on 28/04/2025.
//


import SwiftUI

struct ReportFailureView: View {
    @State private var title: String = ""
    @State private var description: String = ""
    
    var body: some View {
        VStack(spacing: 20) {
            TextField("Tytuł zgłoszenia", text: $title)
                .textFieldStyle(.roundedBorder)
                .padding(.horizontal)
            
            TextField("Opis zgłoszenia", text: $description)
                .textFieldStyle(.roundedBorder)
                .padding(.horizontal)
            
            Button(action: {
                // Tutaj logika wysyłania zgłoszenia
                print("Wysłano: \(title) - \(description)")
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
    }
}
