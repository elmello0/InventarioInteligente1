// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("android") version "1.9.0" apply false
    kotlin("kapt") version "1.9.0" apply false
    id("com.android.application") version "8.1.1" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}

buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("com.google.gms:google-services:4.4.0") // Asegurarte de tener esto para Firebase
    }
}

allprojects {
}
