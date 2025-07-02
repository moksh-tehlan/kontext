import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
}

// Load environment variables from .env files
fun loadEnvFile(fileName: String): Properties {
    val envFile = rootProject.file(fileName)
    val properties = Properties()
    if (envFile.exists()) {
        properties.load(envFile.inputStream())
    }
    return properties
}

val devEnv = loadEnvFile(".env.dev")
val prodEnv = loadEnvFile(".env.prod")

android {
    namespace = "com.moksh.kontext"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.moksh.kontext"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Default BuildConfig fields (will be overridden by build variants)
        buildConfigField(
            "String",
            "API_BASE_URL",
            "\"${devEnv.getProperty("API_BASE_URL") ?: ""}\""
        )
        buildConfigField(
            "long",
            "API_TIMEOUT_SECONDS",
            "${devEnv.getProperty("API_TIMEOUT_SECONDS") ?: "30"}L"
        )
        buildConfigField(
            "String",
            "GCP_CLIENT_ID",
            "\"${devEnv.getProperty("GCP_CLIENT_ID") ?: ""}\""
        )
    }

    signingConfigs {
        create("development") {
            storeFile = file(devEnv.getProperty("KEYSTORE_FILE") ?: "debug.keystore")
            storePassword = devEnv.getProperty("KEYSTORE_PASSWORD") ?: "android"
            keyAlias = devEnv.getProperty("KEY_ALIAS") ?: "kontext-alias"
            keyPassword = devEnv.getProperty("KEY_PASSWORD") ?: "android"
        }
        create("production") {
            storeFile = file(prodEnv.getProperty("KEYSTORE_FILE") ?: "")
            storePassword = prodEnv.getProperty("KEYSTORE_PASSWORD") ?: ""
            keyAlias = prodEnv.getProperty("KEY_ALIAS") ?: ""
            keyPassword = prodEnv.getProperty("KEY_PASSWORD") ?: ""
        }
    }

    flavorDimensions += "environment"

    productFlavors {
        create("development") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "Kontext Dev")
        }
        create("production") {
            dimension = "environment"
            resValue("string", "app_name", "Kontext")
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("development")
            isDebuggable = true
            isMinifyEnabled = false

            buildConfigField(
                "String",
                "BUILD_VARIANT",
                "\"development\""
            )
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"${devEnv.getProperty("API_BASE_URL") ?: ""}\""
            )
            buildConfigField(
                "long",
                "API_TIMEOUT_SECONDS",
                "${devEnv.getProperty("API_TIMEOUT_SECONDS") ?: "30"}L"
            )
            buildConfigField(
                "String",
                "GCP_CLIENT_ID",
                "\"${devEnv.getProperty("GCP_CLIENT_ID") ?: ""}\""
            )
            buildConfigField(
                "String",
                "CONTACT_URL",
                "\"${devEnv.getProperty("CONTACT_URL") ?: ""}\""
            )
            buildConfigField(
                "String",
                "TERMS_URL",
                "\"${devEnv.getProperty("TERMS_URL") ?: ""}\""
            )
            buildConfigField(
                "String",
                "PRIVACY_URL",
                "\"${devEnv.getProperty("PRIVACY_URL") ?: ""}\""
            )
        }
        release {
            signingConfig = signingConfigs.getByName("production")
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField(
                "String",
                "BUILD_VARIANT",
                "\"production\""
            )
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"${prodEnv.getProperty("API_BASE_URL") ?: ""}\""
            )
            buildConfigField(
                "long",
                "API_TIMEOUT_SECONDS",
                "${prodEnv.getProperty("API_TIMEOUT_SECONDS") ?: "60"}L"
            )
            buildConfigField(
                "String",
                "GCP_CLIENT_ID",
                "\"${prodEnv.getProperty("GCP_CLIENT_ID") ?: ""}\""
            )
            buildConfigField(
                "String",
                "CONTACT_URL",
                "\"${prodEnv.getProperty("CONTACT_URL") ?: ""}\""
            )
            buildConfigField(
                "String",
                "TERMS_URL",
                "\"${prodEnv.getProperty("TERMS_URL") ?: ""}\""
            )
            buildConfigField(
                "String",
                "PRIVACY_URL",
                "\"${prodEnv.getProperty("PRIVACY_URL") ?: ""}\""
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.1.21" // Aligned with Kotlin 2.1.21
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

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Shared Preferences
    implementation(libs.androidx.shared.preferences)
    implementation(libs.androidx.security.crypto)

    // Credential Manager (modern Google Sign-In)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // Image Loading
    implementation(libs.coil.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}