plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.kapt) apply false
    alias(libs.plugins.kotlin.jvm) apply false
}

subprojects {
    plugins.withId("maven-publish") {
        configure<PublishingExtension> {
            repositories {
                maven {
                    name = "GitHubPackages"
                    url = uri("https://maven.pkg.github.com/wtw-software/AndroidRestServiceUtils")
                    // Resolved lazily from ORG_GRADLE_PROJECT_GitHubPackagesUsername /
                    // ...Password. Explicit credentials would opt these tasks out of the
                    // configuration cache, and are only required once a publish task is
                    // actually scheduled, so local builds stay credential-free.
                    credentials(PasswordCredentials::class)
                }
            }
        }
    }
}