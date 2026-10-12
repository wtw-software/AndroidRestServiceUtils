plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
}

dependencies {
    api(libs.gson)
    api(libs.commons.io)
    api(libs.joda.time)
    api(libs.okhttp)
    api(libs.okhttp.logging.interceptor)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            groupId = "com.github.wtw-software"
            artifactId = "androidrestserviceutils-core"
            version = System.getenv("VERSION") ?: "SNAPSHOT"
            afterEvaluate { from(components["java"]) }
        }
    }
}
