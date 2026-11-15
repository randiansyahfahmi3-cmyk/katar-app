plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.katar.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.katar.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
        kotlinCompiler = "org.jetbrains.kotlin:kotlin-compiler:1.9.22"
    }

    packagingOptions {
        resources {
            excludes += "META-INF/{Aligent,SF,DSA,license,notice}"
        }
        excludes += "META-INF/kotlinx-atomicvalue.jar"
    }
}

dependencies {

    // AndroidX and Core
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.8.0")

    // Compose Material 3
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.compose.material:material:1.2.0")

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Activity
    implementation("androidx.activity:activity-ktx:1.8.0")

    // Room Database
    implementation("androidx.room:room-ktx:2.5.1")
    implementation("androidx.room:room-rxjava2:2.5.1")
    kapt("androidx.room:room-compiler:2.5.1")

    // Retrofit & OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Lifecycle ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
}