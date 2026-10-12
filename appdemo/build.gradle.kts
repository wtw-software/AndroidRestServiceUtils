plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kapt)
}

android {
    namespace = "no.wtw.android.restserviceutils.appdemo"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "no.wtw.android.restserviceutils.appdemo"
        minSdk = libs.versions.minSdkDemo.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packaging {
        resources {
            excludes += listOf(
                "META-INF/license.txt",
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/notice.txt",
                "META-INF/INDEX.LIST",
                "META-INF/DEPENDENCIES.txt",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE.txt",
                "META-INF/NOTICE",
                "META-INF/LICENSE",
                "META-INF/DEPENDENCIES",
                "META-INF/dependencies.txt",
                "META-INF/LGPL2.1",
                "LICENSE.txt",
                "LICENSE",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":library"))
    implementation(project(":linky-annotation"))
    kapt(project(":linky-processor"))
}
