plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
}

dependencies {
    api(project(":linky-annotation"))
    implementation(project(":library-core"))
    implementation(libs.kotlinpoet)
    implementation(libs.kotlin.reflect)
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
            artifactId = "androidrestserviceutils-linky-processor"
            version = System.getenv("VERSION") ?: "SNAPSHOT"
            afterEvaluate { from(components["java"]) }
        }
    }
}