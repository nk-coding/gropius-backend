package gropius.service.common

import gropius.authorization.GropiusAuthorizationContext
import io.github.graphglue.authorization.AuthorizationChecker
import io.github.graphglue.authorization.Permission
import io.github.graphglue.model.Node
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [Node]
 *
 * @param repository the associated repository used for CRUD functionality
 */
abstract class NodeService<T : Node>(val repository: ReactiveNeo4jRepository<T, String>) {

    /**
     * Injected, used for the [checkPermission] function
     */
    lateinit var authorizationChecker: AuthorizationChecker

    /**
     * Checks if the [permission] is granted on [node]
     * If checkPermission on the `permission.context` is `false`, no permission is evaluated
     * Does not handle the case that the user is an admin
     * If [permission] is `null`, no check is performed
     *
     * @param node the node where the permission must be granted
     * @param permission the permission to check for, none is checked if `null`
     * @param errorMessage the message to throw in case the permission is not granted
     * @throws IllegalArgumentException with the provided [errorMessage] in case the permission is not granted
     */
    suspend fun checkPermission(node: Node, permission: Permission?, errorMessage: String) {
        val checkPermission = permission != null && (permission.context as GropiusAuthorizationContext).checkPermission
        if (checkPermission && !authorizationChecker.hasAuthorization(node, permission!!).awaitSingle()) {
            throw IllegalArgumentException(errorMessage)
        }
    }
}