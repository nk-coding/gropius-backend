val graphglueVersion: String by project

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(path = ":core"))
    implementation("io.github.graphglue", "graphglue", graphglueVersion)
}