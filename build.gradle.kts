import org.jetbrains.dokka.gradle.DokkaTaskPartial

description = "A Cross-Component Issue Management System for Component-based Architectures"

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.dokka")

    tasks.withType<DokkaTaskPartial>().configureEach {
        dokkaSourceSets {
            configureEach {
                includeNonPublic.set(true)
            }
        }
    }
}