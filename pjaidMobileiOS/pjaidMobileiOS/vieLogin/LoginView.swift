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


    var body: some View {
        NavigationStack {
            ZStack {
                Image("background")
                    .resizable()
                    .scaledToFill()
                    .clipped()
                    .ignoresSafeArea()

                VStack(spacing: 16) {
                    Spacer()

                    Image("logo")
                        .resizable()
                        .scaledToFit()
                        .frame(height: 180)
                        .padding(.bottom, 10)

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
                            // akcja
                        }
                        .font(.footnote)
                        .foregroundColor(.white.opacity(0.7))
                    }
                    .padding(.horizontal, 10)
                    .frame(maxWidth: 300)

                    Button(action: {
                        appState.currentUser = email
                        appState.isLoggedIn = true
                    }) {
                        Text("Sign in")
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(Color.blue)
                            .foregroundColor(.white)
                            .cornerRadius(10)
                    }
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
            }
        }
        
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
