package gropius.graphql

import com.expediagroup.graphql.generator.execution.KotlinDataFetcherFactoryProvider
import com.expediagroup.graphql.generator.execution.SimpleKotlinDataFetcherFactoryProvider
import com.expediagroup.graphql.server.spring.execution.DefaultSpringGraphQLContextFactory
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.scalars.regex.RegexScalar
import graphql.schema.*
import gropius.authorization.GropiusAuthorizationContext
import gropius.model.user.GropiusUser
import gropius.model.user.IMSUser
import gropius.repository.user.GropiusUserRepository
import io.github.graphglue.authorization.AuthorizationContext
import io.github.graphglue.connection.filter.TypeFilterDefinitionEntry
import io.github.graphglue.connection.filter.definition.scalars.StringFilterDefinition
import kotlinx.coroutines.reactor.awaitSingle
import org.neo4j.driver.Driver
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.core.ReactiveDatabaseSelectionProvider
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager
import org.springframework.web.reactive.function.server.ServerRequest
import java.net.URI
import java.time.Duration
import java.time.OffsetDateTime
import kotlin.reflect.KFunction
import kotlin.reflect.full.createType

/**
 * Contains bean necessary for GraphQL configuration
 * Provides additional scalars
 */
@Configuration
class GraphQLConfiguration {

    /**
     * Generates the GraphQL context map
     * TODO: use authentication as soon as available
     *
     * @param gropiusUserRepository used to get the user
     * @return the generated context factory
     */
    @Bean
    fun contextFactory(gropiusUserRepository: GropiusUserRepository) = object : DefaultSpringGraphQLContextFactory() {
        override suspend fun generateContextMap(request: ServerRequest): Map<*, Any> {
            //TODO use authentication as soon as available
            val userId = request.headers().firstHeader("Authorization")
            val additionalContextEntries = if (userId == null) {
                emptyMap()
            } else {
                val user = gropiusUserRepository.findById(userId).awaitSingle()
                mapOf(AuthorizationContext::class to GropiusAuthorizationContext(userId, !user.isAdmin))
            }
            return super.generateContextMap(request) + additionalContextEntries
        }
    }

    /**
     * Necessary transaction manager
     *
     * @param driver used Neo4j driver
     * @param databaseNameProvider Neo4j database provider
     * @return the generated transaction manager
     */
    @Bean
    fun reactiveTransactionManager(
        driver: Driver, databaseNameProvider: ReactiveDatabaseSelectionProvider
    ): ReactiveNeo4jTransactionManager {
        return ReactiveNeo4jTransactionManager(driver, databaseNameProvider)
    }

    /**
     * JSON scalar which can be used by annotating a function / property with `@GraphQLType("JSON")`
     *
     * @return the provided JSON scalar
     */
    @Bean
    fun jsonScalar(objectMapper: ObjectMapper) =
        GraphQLScalarType.newScalar().name("JSON").description("A JSON scalar").coercing(JSONCoercing(objectMapper))
            .build()

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
     * SchemaGeneratorHooks which support the [TypeGraphQLType] annotation
     * Also adds support for scalars:
     * - [OffsetDateTime] -> DateTime
     * - [URI] -> Url
     * - Duration -> Duration
     * Handles the automatic generation of payload types for mutations annotated with [AutoPayloadType]
     */
    @Bean
    fun schemaGeneratorHooks() = DefaultSchemaGeneratorHooks

    /**
     * Filter factory for [OffsetDateTime] properties
     *
     * @return the generated filter factory
     */
    @Bean
    fun dateTimeFilter() =
        TypeFilterDefinitionEntry(OffsetDateTime::class.createType(nullable = true)) { name, property, parentNodeDefinition, _ ->
            DateTimeFilterDefinition(
                name, parentNodeDefinition.getNeo4jNameOfProperty(property), property.returnType.isMarkedNullable
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
                name, parentNodeDefinition.getNeo4jNameOfProperty(property), property.returnType.isMarkedNullable
            )
        }

    /**
     * Filter factory for [URI] properties
     *
     * @return the generated filter factory
     */
    @Bean
    fun urlFilter() =
        TypeFilterDefinitionEntry(URI::class.createType(nullable = true)) { name, property, parentNodeDefinition, _ ->
            StringFilterDefinition(
                name, parentNodeDefinition.getNeo4jNameOfProperty(property), property.returnType.isMarkedNullable
            )
        }

    /**
     * Filter for usernames on both [IMSUser] and [GropiusUser]
     *
     * @return the generated filter definition
     */
    @Bean("usernameFilter")
    fun usernameFilter() = StringFilterDefinition("username", "username", true)

    /**
     * Provides the [KotlinDataFetcherFactoryProvider] which generates a FunctionDataFetcher which handles
     * JSON input value injecting correctly.
     *
     * @param applicationContext used to obtain Spring beans
     * @return the generated [KotlinDataFetcherFactoryProvider], used by graphql-kotlin
     */
    @Bean
    fun kotlinDataFetcherFactory(applicationContext: ApplicationContext): KotlinDataFetcherFactoryProvider =
        object : SimpleKotlinDataFetcherFactoryProvider() {
            override fun functionDataFetcherFactory(target: Any?, kFunction: KFunction<*>) = DataFetcherFactory {
                GropiusFunctionDataFetcher(target, kFunction, applicationContext)
            }
        }

}