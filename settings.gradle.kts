pluginManagement {
    val springBootVersion: String by settings
    val kotlinVersion: String by settings
    val apolloVersion: String by settings

    plugins {
        id("org.springframework.boot") version springBootVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        id("org.jetbrains.dokka") version kotlinVersion
        id("com.apollographql.apollo3") version apolloVersion
    }
}

include(":core")
include(":api")
include(":github")
include(":sync")