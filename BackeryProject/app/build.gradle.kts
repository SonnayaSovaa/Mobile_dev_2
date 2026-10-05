plugins {
    alias(libs.plugins.android.application)
    // alias(libs.plugins.google.services) // Commented out to fix missing google-services.json error
}

android {
    namespace = "ru.mirea.nagishevakv.backeryproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.mirea.nagishevakv.backeryproject"
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
    
    // TensorFlow Lite models are usually compressed, we need to tell aapt not to compress them
    aaptOptions {
        noCompress("tflite")
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    implementation(libs.room.runtime)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    annotationProcessor(libs.room.compiler)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    
    // Retrofit for Weather API
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // TensorFlow Lite
    implementation(libs.tensorflow.lite)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
    implementation(libs.tensorflow.lite.task.text)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}