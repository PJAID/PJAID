plugins {
    alias(libs.plugins.android.application)
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.pjaidmobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.pjaidmobile"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.zxing.android.embedded)
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
// RxJava3
    implementation(libs.rxandroid) // Użyj najnowszej wersji
    implementation(libs.rxjava)
// Retrofit RxJava Adapter
    implementation (libs.retrofit2.adapter.rxjava3) // Użyj wersji pasującej do Retrofit
// Lifecycle (ViewModel, LiveData)
    implementation (libs.lifecycle.viewmodel) // Użyj najnowszej wersji
    implementation (libs.androidx.lifecycle.livedata)
// Do integracji Java 8 z Lifecycle (opcjonalnie, ale pomocne)
    implementation (libs.lifecycle.common.java8)
    // Google Maps
    implementation(libs.play.services.maps)

// Lokalizacja
    implementation(libs.play.services.location)

// Hilt
    implementation (libs.hilt.android) // Użyj najnowszej wersji
    annotationProcessor (libs.dagger.hilt.compiler) // Jeśli nie używasz Kapt
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}