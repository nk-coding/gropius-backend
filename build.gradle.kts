description = "A Cross-Component Issue Management System for Component-based Architectures"

plugins {
    id("org.springframework.boot") version "2.6.7"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
    id("com.expediagroup.graphql") version "6.0.0-alpha.2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.graphglue", "graphglue", "2.0.0")
    implementation("com.graphql-java","graphql-java-extended-scalars", "18.0")
}

graphql {
    schema {
        packages = listOf("gropius")
    }
}
