@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.hilt)
}

apply {
    from("$rootDir/buildConfig/common-config.gradle")
}

android {
    namespace = "com.example.musicplayerkts"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.musicplayerkts"
        minSdk = 24
        compileSdk = 33

        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.play.services)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.datastore.datastore.preferences)
    implementation(libs.material)
    implementation(libs.paging.runtime)
    implementation(libs.constraintlayout)
    implementation(libs.hilt.android)
    implementation(libs.timber)
    implementation(libs.com.android.support.multidex)
    implementation(libs.gson)
    implementation(libs.io.gresse.hugo.vumeterlibrary)

    implementation(libs.com.squareup.okhttp3.logging.interceptor)
    implementation(libs.com.squareup.retrofit2.retrofit)
    implementation(libs.com.squareup.retrofit2.converter.gson)
    implementation(libs.com.squareup.retrofit2.adapter.rxjava2)

    implementation(libs.io.insert.koin.koin.android)
    implementation(libs.io.insert.koin.koin.android.compat)
    implementation(libs.io.insert.koin.koin.androidx.navigation)
    implementation(libs.io.insert.koin.koin.androidx.workmanager)

    implementation(libs.com.jakewharton.threetenabp.threetenabp)
    implementation(libs.com.jakewharton.retrofit.retrofit2.kotlin.coroutines.adapter)
    implementation(libs.com.github.f0ris.sweetalert.library)

    implementation(libs.com.github.bumptech.glide.glide)
    annotationProcessor(libs.com.github.bumptech.glide.compiler)


    kapt(libs.hilt.compiler)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.release)
}