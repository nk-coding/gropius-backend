val graphglueVersion: String by project

plugins {
    kotlin("plugin.spring")
}

dependencies {
    api(project(path = ":core"))
    api("io.github.graphglue", "graphglue", graphglueVersion)
}