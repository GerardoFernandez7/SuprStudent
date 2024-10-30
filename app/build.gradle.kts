plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
}

android {
    namespace = "com.joseruiz.suprstudent"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.joseruiz.suprstudent"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        /***********************************/
        // Usa findProperty para obtener la clave API de gradle.properties
        val apiKey: String = project.findProperty("API_NINJAS_KEY") as String? ?: ""
        buildConfigField("String", "API_NINJAS_KEY", "\"$apiKey\"")

        buildFeatures {
            buildConfig = true  // Habilitar BuildConfig
            compose = true
        }
        /***************************************/

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Navigation
    implementation(libs.androidx.navigation.compose)
    // Room
    implementation("androidx.room:room-runtime:2.5.1")
    ksp("androidx.room:room-compiler:2.5.1")
    implementation("androidx.room:room-ktx:2.5.1")
    //<Firebase SDK dependencies>
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    //<Firebase SDK dependencies/>

    //Compose ViewMode
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    //Network calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //json to Kotlin object mapping
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //Image loading
    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation(kotlin("script-runtime"))

    implementation(libs.firebase.bom)
    implementation (libs.google.firebase.storage.ktx)
    implementation (libs.google.firebase.firestore.ktx)

}