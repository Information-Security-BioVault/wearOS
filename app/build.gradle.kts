plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    // 파이썬 코드 실행을 위한 Chaquopy 플러그인 추가
//    id("com.chaquo.python")
}

android {
    namespace = "com.project.biovaultwatch"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.project.biovaultwatch"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }

        // 파이썬 코드 실행을 위한 Chaquopy 플러그인 추가
//        ndk {
//            // On Apple silicon, you can omit x86_64.
//            abiFilters += listOf("arm64-v8a", "x86_64", "armeabi-v7a")
//        }

        /*
        flavorDimensions += "pyVersion"
        productFlavors {
//            create("py38") { dimension = "pyVersion" }
            create("py39") { dimension = "pyVersion" }
//            create("py310") { dimension = "pyVersion" }
//            create("py311") { dimension = "pyVersion" }
        }
         */

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
    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// 파이썬 코드 실행을 위한 Chaquopy 플러그인 추가
/* chaquopy {
    defaultConfig {
        // 주석된 buildPython 설정 제거
//        buildPython("3.9.6")

        pip {
            install("numpy")
            install("pi-heaan")
            install("approx")
            install("pandas")
            install("tqdm")
            install("python-math")
//            install("os-sys")
//            install("TIME-python")
        }
    }

    productFlavors {
//        getByName("py38") { version = "3.8" }
        getByName("py39") { version = "3.9" }
//        getByName("py310") { version = "3.10" }
//        getByName("py311") { version = "3.11" }
    }

    sourceSets.getByName("main") {
        setSrcDirs(listOf("src/main/python"))
    }
}
 */



dependencies {

    implementation(libs.play.services.wearable)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.activity.compose)
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation(libs.core.splashscreen)
    implementation(libs.media3.common)
    implementation("androidx.health:health-services-client:1.0.0-alpha02")
    implementation("com.google.guava:guava:30.1.1-android")
    implementation("androidx.concurrent:concurrent-futures-ktx:1.1.0")
    //fitness api
//    implementation("com.google.android.gms:play-services-fitness:21.1.0")
//    implementation("com.google.android.gms:play-services-auth:20.7.0")
    //samsung healthcare
//    implementation("com.samsung.android.sdk.healthdata:health-data:2.16.0")
//    implementation("com.samsung.android.sdk:healthdata-store:2.16.0")
    implementation(libs.play.services.fitness)
    implementation(libs.material3.android)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.animation.graphics.android)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation("io.coil-kt:coil-gif:2.0.0-rc02")
    implementation("io.coil-kt:coil-compose:2.0.0-rc02")
    // api
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // JSON 변환
    implementation("com.squareup.okhttp3:okhttp:4.10.0") // OkHttp 라이브러리
    implementation("com.squareup.okhttp3:logging-interceptor:3.11.0")
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.9.0")

    // 파이썬 코드 실행을 위한 Chaquopy 플러그인 추가
//    implementation("com.chaquo.python:python:15.0.1")
}