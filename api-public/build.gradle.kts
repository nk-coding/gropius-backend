val jjwtVersion: String by project

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(path = ":api-common"))
    implementation("io.jsonwebtoken", "jjwt-api", jjwtVersion)
    implementation("io.jsonwebtoken", "jjwt-impl", jjwtVersion)
    implementation("io.jsonwebtoken", "jjwt-jackson", jjwtVersion)
}