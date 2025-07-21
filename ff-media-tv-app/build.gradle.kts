plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    kotlin("plugin.serialization") version "1.9.0"
    id("kotlin-parcelize")
}

android {
    namespace = "com.faithForward.media"
    compileSdk = 35

    signingConfigs {
        create("release") {
            keyAlias = "ff-media-keyStore"
            keyPassword = "12345678"
            storeFile = file("../keystore/ff-media-keyStore.jks")
            storePassword = "12345678"
        }
    }

    lint {
        disable.add("NullSafeMutableLiveData")
    }

    defaultConfig {
        applicationId = "com.faithForward.media"
        minSdk = 24
        targetSdk = 35
        versionCode = 4
        versionName = "3.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = true
            isMinifyEnabled = true
            isShrinkResources = true
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //coil
    implementation(libs.coil.compose)
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
    //constraintlayout
    implementation(libs.androidx.constraintlayout.compose)

    // Dependency injection
    implementation(libs.hilt.android)
    implementation(libs.androidx.ui.graphics.android)
    implementation(libs.play.services.ads.identifier)
    kapt(libs.hilt.compiler)

    //navigation for compose
    implementation("androidx.navigation:navigation-compose:2.9.0")

    // data module
    implementation(project(":ff-media-Data"))

    //media
    val media3 = "1.4.1"
    implementation("androidx.media3:media3-exoplayer:$media3")
    implementation("androidx.media3:media3-ui:$media3")
    implementation("androidx.media3:media3-common:$media3")
    implementation("androidx.media3:media3-exoplayer-dash:$media3")
    implementation("androidx.media3:media3-exoplayer-hls:$media3")

    // implementation(libs.kotlinx.coroutines.android)

    //kotlin ktx viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //viewmodel with hilt
    implementation("androidx.hilt:hilt-work:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    //material
    implementation("androidx.compose.material:material:1.8.2")


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}