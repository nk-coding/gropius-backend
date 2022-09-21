package gropius.graphql

import com.expediagroup.graphql.server.spring.execution.DefaultSpringGraphQLContextFactory
import gropius.GropiusInternalApiConfigurationProperties
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ResponseStatusException

/**
 * Generates the GraphQL context map
 *
 * @param gropiusInternalApiConfigurationProperties used to determine if authentication is optional
 */
@Component
class GropiusGraphQLContextFactory(
    private val gropiusInternalApiConfigurationProperties: GropiusInternalApiConfigurationProperties
) : DefaultSpringGraphQLContextFactory() {

    override suspend fun generateContextMap(request: ServerRequest): Map<*, Any> {
        val token = request.headers().firstHeader("Authorization")?.replace("Bearer ", "", true) ?: ""
        if (gropiusInternalApiConfigurationProperties.apiToken.let { it != null && it != token }) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "No or invalid authentication token provided")
        }
        return super.generateContextMap(request)
    }

}