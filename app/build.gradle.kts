plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.plugin)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.example.livesaver"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.livesaver"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Dagger Hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    //coroutines and runtime
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.runtime.livedata)
    //Datastore
    implementation(libs.androidx.datastore.preferences)

}