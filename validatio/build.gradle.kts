plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(Android.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Android.minSdkVersion)
        targetSdkVersion(Android.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", Versions.kotlin))

    // Tests
    testImplementation(Core.junit)
    androidTestImplementation(Core.androidxTestRunner)
    androidTestImplementation(Core.androidxEspresso)
    testImplementation(Core.robolectric)
    testImplementation(Core.mockito)
    testImplementation(Core.mockitoKotlin)

    // Android
    implementation(Core.androidxAppCompat)
    implementation(Core.androidxKtx)

    // RxJava
    implementation(Libs.rxJava2)
    implementation(Libs.rxAndroid)
    implementation(Libs.rxKotlin)
}
