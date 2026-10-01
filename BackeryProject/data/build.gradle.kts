plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "ru.mirea.nagishevakv.backeryproject.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.appcompat)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.lifecycle.livedata)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    
    // Retrofit for Weather API
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
}