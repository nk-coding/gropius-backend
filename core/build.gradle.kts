val graphglueVersion: String by project
val graphqlJavaVersion: String by project
val jsonSchemaValidatorVersion: String by project

plugins {
    kotlin("plugin.spring")
}

dependencies {
    api("org.springframework.data", "spring-data-neo4j") {
        version {
            strictly("6.3.3-SNAPSHOT")
        }
    }
    api("io.github.graphglue", "graphglue-core", graphglueVersion)
    api("com.graphql-java","graphql-java-extended-scalars", graphqlJavaVersion)
    api("com.networknt", "json-schema-validator", jsonSchemaValidatorVersion)
}