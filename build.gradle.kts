buildscript {

    repositories {
        google()
        mavenCentral()

    }

    dependencies {
        //classpath("com.google.gms:google-services:4.4.2")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath ("com.android.tools.build:gradle:8.1.4")  // Use the latest compatible version

    }

}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false

}



