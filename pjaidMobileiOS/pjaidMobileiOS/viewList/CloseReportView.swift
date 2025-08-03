//
//  CloseReportView.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 03/08/2025.
//
import SwiftUI

struct CloseReportView: View {
    let ticketId: Int
    var onClose: () -> Void = {}

    @Environment(\.dismiss) var dismiss
    @State private var summary: String = ""
    @State private var timeSpent: String = ""
    @State private var notes: String = ""
    @State private var showSuccessAlert = false
    @State private var showConfirmation = false
    @State private var isSubmitting = false

    var body: some View {
        Form {
            Section(header: Text("Podsumowanie")) {
                TextField("Opis działań", text: $summary)
            }

            Section(header: Text("Czas (minuty)")) {
                TextField("Czas pracy", text: $timeSpent)
                    .keyboardType(.numberPad)
            }

            Section(header: Text("Uwagi")) {
                TextField("Dodatkowe uwagi", text: $notes)
            }

            Section {
                Button("Zapisz i zamknij zgłoszenie") {
                    saveToServer()
                }
                .disabled(isSubmitting)
                .foregroundColor(.blue)

                Button("Eksportuj CSV") {
                    exportToCSV()
                }
                .foregroundColor(.green)

                Button("Eksportuj XLS") {
                    showConfirmation = true
                }
                .foregroundColor(.gray)
            }
        }
        .navigationTitle("Zamknij zgłoszenie")
        .alert(isPresented: $showSuccessAlert) {
            Alert(
                title: Text("Sukces"),
                message: Text("Zgłoszenie zostało zamknięte."),
                dismissButton: .default(Text("OK")) {
                    onClose()
                    dismiss()
                }
            )
        }
        .alert(isPresented: $showConfirmation) {
            Alert(
                title: Text("Eksport XLS"),
                message: Text("Eksport do XLS niezaimplementowany"),
                dismissButton: .default(Text("OK"))
            )
        }
    }

    func saveToServer() {
        guard let url = URL(string: "http://localhost:8080/ticket/\(ticketId)/finish") else { return }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")

        let body = [
            "summary": summary,
            "timeSpent": timeSpent,
            "notes": notes
        ]

        do {
            let jsonData = try JSONSerialization.data(withJSONObject: body)
            request.httpBody = jsonData
        } catch {
            print("Błąd serializacji JSON: \(error)")
            return
        }

        isSubmitting = true

        URLSession.shared.dataTask(with: request) { _, _, error in
            DispatchQueue.main.async {
                isSubmitting = false
                if let error = error {
                    print("Błąd: \(error)")
                    return
                }
                showSuccessAlert = true
            }
        }.resume()
    }

    func exportToCSV() {
        let csv = "Zgłoszenie ID,Czas,Działania,Uwagi\n\(ticketId),\(timeSpent),\(summary),\(notes)\n"
        let tempURL = FileManager.default.temporaryDirectory.appendingPathComponent("raport_zgloszenia.csv")
        do {
            try csv.write(to: tempURL, atomically: true, encoding: .utf8)
            print("CSV zapisany: \(tempURL)")
        } catch {
            print("Błąd przy zapisie CSV: \(error)")
        }
    }
}
