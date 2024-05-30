// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven {url = uri("https://chaquo.com/maven/")}  // 파이썬 코드 실행을 위한 Chaquopy 플러그인 추가
        maven {url = uri("https://jitpack.io")}
        jcenter() // Warning: this repository is going to shut down soon
    }

    // 파이썬 코드 실행을 위한 Chaquopy 플러그인 추가
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("com.chaquo.python:gradle:15.0.1")
    }
}
