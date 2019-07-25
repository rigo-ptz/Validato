buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.androidGradle}")
        classpath(kotlin("gradle-plugin", Versions.kotlin))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
