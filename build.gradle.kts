description = "A Cross-Component Issue Management System for Component-based Architectures"

plugins {
    id("org.springframework.boot") version "2.7.0"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.graphglue", "graphglue", "4.0.0")
    implementation("com.graphql-java","graphql-java-extended-scalars", "18.0")
}
