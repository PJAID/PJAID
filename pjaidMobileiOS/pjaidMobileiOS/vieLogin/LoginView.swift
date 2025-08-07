//
//  LoginView.swift
//  pjaidMobileiOS
//
//  Created by Jakub Marcinkowski on 07/05/2025.
//

import SwiftUI
import LocalAuthentication

struct LoginView: View {
    @EnvironmentObject var appState: AppState

    @State private var email: String = ""
    @State private var password: String = ""
    @State private var isPasswordVisible: Bool = false
    @State private var rememberMe: Bool = false
    @State private var faceIDError: String?
    @State private var showingFaceIDError = false

    @State private var loginError: String?
    @State private var isLoading = false

    var body: some View {
        NavigationStack {
            ZStack {
                Image("background")
                    .resizable()
                    .scaledToFill()
                    .ignoresSafeArea()

                VStack(spacing: 16) {
                    Spacer()

                    Image("logo")
                        .resizable()
                        .scaledToFit()
                        .frame(height: 180)

                    TextField("Username or email", text: $email)
                        .textFieldStyle(.roundedBorder)
                        .frame(maxWidth: 300)

                    ZStack {
                        HStack {
                            if isPasswordVisible {
                                TextField("Password", text: $password)
                            } else {
                                SecureField("Password", text: $password)
                            }

                            Button(action: {
                                isPasswordVisible.toggle()
                            }) {
                                Image(systemName: isPasswordVisible ? "eye.slash" : "eye")
                                    .foregroundColor(.gray)
                            }
                        }
                    }
                    .padding()
                    .frame(height: 44)
                    .background(Color.white)
                    .cornerRadius(8)
                    .shadow(radius: 1)
                    .frame(maxWidth: 300)

                    HStack {
                        Toggle("Remember me", isOn: $rememberMe)
                            .toggleStyle(CheckboxToggleStyle())

                        Spacer()

                        Button("Forgot password?") {
                            // implement later
                        }
                        .font(.footnote)
                        .foregroundColor(.white.opacity(0.7))
                    }
                    .padding(.horizontal, 10)
                    .frame(maxWidth: 300)

                    if let loginError = loginError {
                        Text(loginError)
                            .foregroundColor(.red)
                            .multilineTextAlignment(.center)
                    }

                    Button(action: {
                        login()
                    }) {
                        if isLoading {
                            ProgressView().frame(maxWidth: 300)
                        } else {
                            Text("Sign in")
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color.blue)
                                .foregroundColor(.white)
                                .cornerRadius(10)
                        }
                    }
                    .disabled(email.isEmpty || password.isEmpty)
                    .frame(maxWidth: 300)

                    Button(action: {
                        authenticateWithFaceID()
                    }) {
                        HStack {
                            Image(systemName: "faceid")
                            Text("Zaloguj się Face ID")
                        }
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.green)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                    }
                    .frame(maxWidth: 300)

                    Spacer()
                }
                .alert(isPresented: $showingFaceIDError) {
                    Alert(
                        title: Text("Błąd Face ID"),
                        message: Text(faceIDError ?? "Nieznany błąd"),
                        dismissButton: .default(Text("OK"))
                    )
                }
                .padding(.top)
                .padding(.horizontal)
            }
        }
    }

    func login() {
        isLoading = true
        loginError = nil

        guard let url = URL(string: "http://localhost:8080/api/auth/login") else {
            loginError = "Błędny adres serwera"
            isLoading = false
            return
        }

        let loginPayload = ["username": email, "password": password]
        guard let jsonData = try? JSONSerialization.data(withJSONObject: loginPayload) else {
            loginError = "Nie udało się przygotować danych"
            isLoading = false
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = jsonData

        URLSession.shared.dataTask(with: request) { data, response, error in
            DispatchQueue.main.async { isLoading = false }

            if let error = error {
                DispatchQueue.main.async {
                    loginError = "Błąd połączenia: \(error.localizedDescription)"
                }
                return
            }

            guard let data = data else {
                DispatchQueue.main.async {
                    loginError = "Brak danych z serwera"
                }
                return
            }

            do {
                if let httpResponse = response as? HTTPURLResponse {
                    if httpResponse.statusCode == 200 {
                        // poprawne dane logowania
                        if let decoded = try JSONSerialization.jsonObject(with: data) as? [String: Any],
                           let userLogin = decoded["username"] as? String {
                            DispatchQueue.main.async {
                                print("Zalogowano jako: \(userLogin)")
                                appState.currentUser = userLogin.lowercased()
                                appState.isLoggedIn = true
                            }
                        } else {
                            DispatchQueue.main.async {
                                loginError = "Błąd w danych użytkownika"
                            }
                        }
                    } else if httpResponse.statusCode == 401 {
                        DispatchQueue.main.async {
                            loginError = "Niepoprawny login lub hasło"
                        }
                    } else {
                        DispatchQueue.main.async {
                            loginError = "Nieznany błąd (kod \(httpResponse.statusCode))"
                        }
                    }
                } else {
                    DispatchQueue.main.async {
                        loginError = "Nieprawidłowa odpowiedź z serwera"
                    }
                }
            } catch {
                DispatchQueue.main.async {
                    loginError = "Błąd dekodowania odpowiedzi"
                }
            }
        }.resume()
    }

    func authenticateWithFaceID() {
        let context = LAContext()
        var error: NSError?

        if context.canEvaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, error: &error) {
            let reason = "Zaloguj się za pomocą Face ID"

            context.evaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, localizedReason: reason) { success, authenticationError in
                DispatchQueue.main.async {
                    if success {
                        appState.currentUser = "FaceID_User"
                        appState.isLoggedIn = true
                    } else {
                        faceIDError = authenticationError?.localizedDescription ?? "Nieznany błąd"
                        showingFaceIDError = true
                    }
                }
            }
        } else {
            faceIDError = error?.localizedDescription ?? "Face ID niedostępne"
            showingFaceIDError = true
        }
    }
}

struct CheckboxToggleStyle: ToggleStyle {
    func makeBody(configuration: Configuration) -> some View {
        HStack {
            Image(systemName: configuration.isOn ? "checkmark.square.fill" : "square")
                .foregroundColor(.white)
                .onTapGesture { configuration.isOn.toggle() }
            configuration.label
                .foregroundColor(.white)
        }
    }
}
