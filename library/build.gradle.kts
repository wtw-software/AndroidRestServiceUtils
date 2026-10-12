plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
}

android {
    namespace = "no.wtw.android.restserviceutils"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "library" to "true",
                    "androidManifestFile" to "$projectDir/src/main/AndroidManifest.xml",
                )
            }
        }
    }

    packaging {
        resources {
            excludes += listOf(
                "META-INF/license.txt",
                "META-INF/notice.txt",
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

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    api(project(":library-core"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.wtw-software"
            artifactId = "androidrestserviceutils"
            version = System.getenv("VERSION") ?: "SNAPSHOT"
            afterEvaluate { from(components["release"]) }
        }
    }
}
