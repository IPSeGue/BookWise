plugins {
    id("com.android.application") version "8.7.2"
    id("org.jetbrains.kotlin.android") version "2.0.0"
    id("com.google.gms.google-services") version "4.3.15"
}

android {
    namespace = "com.mobdeve.s12.bookwise"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mobdeve.s12.bookwise"
        minSdk = 24
        targetSdk = 34
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

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // AndroidX and Material
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0-alpha04")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Retrofit for API integration
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // Firebase
    implementation("com.google.firebase:firebase-auth:22.1.0")
    implementation("com.google.firebase:firebase-firestore:24.9.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-database:20.2.3")
    implementation("com.google.firebase:firebase-analytics:21.3.0")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Facebook SDK
    implementation("com.facebook.android:facebook-login:16.0.0")

    // Twitter SDK
    implementation("com.twitter.sdk.android:twitter-core:3.3.0")
    implementation("com.twitter.sdk.android:twitter:3.3.0")

    // BCrypt for password hashing
    implementation("org.mindrot:jbcrypt:0.4")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Compose Testing
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Java 8 Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
}
