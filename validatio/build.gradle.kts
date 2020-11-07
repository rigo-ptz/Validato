plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("com.github.dcendents.android-maven")
}

group ="com.github.rigo-ptz"

android {
    compileSdkVersion(Android.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Android.minSdkVersion)
        targetSdkVersion(Android.targetSdkVersion)
        versionCode = 3
        versionName = "1.1.1"
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
    testImplementation(Core.coreTests)

    // Android
    implementation(Core.androidxAppCompat)
    implementation(Core.androidxKtx)
    implementation(Core.androidxLiveData)
}
