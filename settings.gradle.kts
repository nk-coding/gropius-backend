pluginManagement {
    val springBootVersion: String by settings
    val kotlinVersion: String by settings
    val apolloVersion: String by settings
    val nodeGradleVersion: String by settings

    plugins {
        id("org.springframework.boot") version springBootVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        id("org.jetbrains.dokka") version kotlinVersion
        id("com.apollographql.apollo3") version apolloVersion
        id("com.github.node-gradle.node") version nodeGradleVersion
    }
}

include(":core")
include(":api-common")
include(":api-public")
include(":api-internal")
include(":github")
include(":sync")
include(":login-service")