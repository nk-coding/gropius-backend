package gropius.graphql

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.scalars.datetime.DateTimeScalar
import graphql.scalars.`object`.JsonScalar
import graphql.scalars.regex.RegexScalar
import graphql.scalars.url.UrlScalar
import graphql.schema.GraphQLType
import io.github.graphglue.connection.filter.TypeFilterDefinitionEntry
import io.github.graphglue.connection.filter.definition.scalars.StringFilterDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URL
import java.time.Duration
import java.time.OffsetDateTime
import kotlin.reflect.KType
import kotlin.reflect.full.createType

/**
 * Contains bean necessary for GraphQL configuration
 * Provides additional scalars
 */
@Configuration
class GraphQLConfiguration {

    /**
     * JSON scalar which can be used by annotating a function / property with `@GraphQLType("JSON")`
     *
     * @return the provided JSON scalar
     */
    @Bean
    fun jsonScalar() = JsonScalar.INSTANCE

    /**
     * Color scalar which can be used by annotating a function / property with `@GraphQLType("Color")`
     *
     * @return the provided Color scalar
     */
    @Bean
    fun colorScalar() = RegexScalar.Builder().name("Color")
        .description("A color string with the format #[0-9a-fA-F]{6} with the semantics #RRGGBB")
        .addPattern("#[0-9a-fA-F]{6}".toRegex().toPattern())

    /**
     * SchemaGeneratorHooks which adds support for scalars:
     * - [OffsetDateTime] -> DateTime
     * - [URL] -> Url
     * - Duration -> Duration
     */
    @Bean
    fun schemaGeneratorHooks() = object : SchemaGeneratorHooks {
        override fun willGenerateGraphQLType(type: KType): GraphQLType? {
            return when (type.classifier) {
                OffsetDateTime::class -> DateTimeScalar.INSTANCE
                URL::class -> UrlScalar.INSTANCE
                Duration::class -> DurationScalar
                else -> null
            }
        }
    }

    /**
     * Filter factory for [OffsetDateTime] properties
     *
     * @return the generated filter factory
     */
    @Bean
    fun dateTimeFilter() =
        TypeFilterDefinitionEntry(OffsetDateTime::class.createType(nullable = true)) { name, property, parentNodeDefinition, _ ->
            DateTimeFilterDefinition(
                name,
                parentNodeDefinition.getNeo4jNameOfProperty(property),
                property.returnType.isMarkedNullable
            )
        }

    /**
     * Filter factory for [Duration] properties
     *
     * @return the generated filter factory
     */
    @Bean
    fun durationFilter() =
        TypeFilterDefinitionEntry(Duration::class.createType(nullable = true)) { name, property, parentNodeDefinition, _ ->
            DurationFilterDefinition(
                name,
                parentNodeDefinition.getNeo4jNameOfProperty(property),
                property.returnType.isMarkedNullable
            )
        }

    /**
     * Filter factory for [URL] properties
     *
     * @return the generated filter factory
     */
    @Bean
    fun urlFilter() =
        TypeFilterDefinitionEntry(URL::class.createType(nullable = true)) { name, property, parentNodeDefinition, _ ->
            StringFilterDefinition(
                name,
                parentNodeDefinition.getNeo4jNameOfProperty(property),
                property.returnType.isMarkedNullable
            )
        }

    @Bean("usernameFilter")
    fun usernameFilter() = StringFilterDefinition("username", "username", true)
}