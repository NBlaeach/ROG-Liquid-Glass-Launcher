plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "id.rog.ios26launcher"
    compileSdk = 35
    defaultConfig {
        applicationId = "id.rog.ios26launcher"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }


    signingConfigs {
        create("rogRelease") {
            storeFile = file("rog-release.jks")
            storePassword = System.getenv("ROG_STORE_PASSWORD") ?: ""
            keyAlias = System.getenv("ROG_KEY_ALIAS") ?: "roglauncher"
            keyPassword = System.getenv("ROG_KEY_PASSWORD") ?: ""
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("rogRelease")
        }
    }

    buildFeatures { viewBinding = false }
}
