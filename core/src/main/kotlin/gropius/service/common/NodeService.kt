package gropius.service.common

import gropius.authorization.GropiusAuthorizationContext
import gropius.model.user.GropiusUser
import gropius.repository.user.GropiusUserRepository
import io.github.graphglue.authorization.AuthorizationChecker
import io.github.graphglue.authorization.Permission
import io.github.graphglue.model.Node
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [Node]
 *
 * @param repository the associated repository used for CRUD functionality
 * @param T the type of Node this service is used for
 * @param R Repository type associated with [T]
 */
abstract class NodeService<T : Node, R : ReactiveNeo4jRepository<T, String>>(val repository: R) {

    /**
     * Injected, used for the [checkPermission] function
     */
    @Autowired
    lateinit var authorizationChecker: AuthorizationChecker

    /**
     * Injected, used to get a user based on a [GropiusAuthorizationContext]
     */
    @Autowired
    lateinit var gropiusUserRepository: GropiusUserRepository

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

    /**
     * Gets a [GropiusUser] based on the userId from the [authorizationContext]
     *
     * @param authorizationContext used to get the user id, must be a [GropiusAuthorizationContext]
     * @return the found user
     */
    suspend fun getUser(authorizationContext: GropiusAuthorizationContext): GropiusUser {
        return gropiusUserRepository.findById(authorizationContext.userId).awaitSingle()
    }
}