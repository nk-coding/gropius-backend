package gropius.graphql

import com.expediagroup.graphql.server.spring.execution.DefaultSpringGraphQLContextFactory
import gropius.authorization.GropiusAuthorizationContext
import gropius.model.user.permission.NodePermission
import gropius.repository.user.GropiusUserRepository
import io.github.graphglue.authorization.AuthorizationContext
import io.github.graphglue.authorization.Permission
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

/**
 * Generates the GraphQL context map
 * TODO: use authentication as soon as available
 *
 * @param gropiusUserRepository used to get the user
 */
@Component
class GropiusGraphQLContextFactory(
    private val gropiusUserRepository: GropiusUserRepository
) : DefaultSpringGraphQLContextFactory() {

    override suspend fun generateContextMap(request: ServerRequest): Map<*, Any> {
        //TODO use authentication as soon as available
        val userId = request.headers().firstHeader("Authorization")
        val additionalContextEntries = if (userId == null) {
            emptyMap()
        } else {
            val user = gropiusUserRepository.findById(userId).awaitSingle()
            val context = GropiusAuthorizationContext(userId, !user.isAdmin)
            if (user.isAdmin) {
                mapOf(AuthorizationContext::class to context)
            } else {
                mapOf(Permission::class to Permission(NodePermission.READ, context))
            }

        }
        return super.generateContextMap(request) + additionalContextEntries
    }

}