plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.proyecto"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.proyecto"
        minSdk = 26
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
    buildToolsVersion = "35.0.0"
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    implementation(libs.protolite.well.known.types)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.media3.exoplayer)
    implementation(libs.compiler)
    implementation(libs.cronet.embedded)
    implementation(libs.play.services.maps)
    implementation(libs.room.common.jvm)
    implementation(libs.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation ("com.loopj.android:android-async-http:1.4.11")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    //Room
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.libraries.places:places:3.3.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")
}