description = "A Cross-Component Issue Management System for Component-based Architectures"

plugins {
	kotlin("jvm")
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

subprojects {
    apply(plugin = "kotlin")
}
