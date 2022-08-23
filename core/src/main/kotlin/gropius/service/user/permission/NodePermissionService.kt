package gropius.service.user.permission

import gropius.model.user.GropiusUser
import gropius.model.user.permission.NodePermission
import gropius.repository.GropiusRepository

/**
 * Base class for services for subclasses of [NodePermission]
 *
 * @param repository the associated repository used for CRUD functionality
 * @param T the type of Node this service is used for
 * @param R Repository type associated with [T]
 */
abstract class NodePermissionService<T : NodePermission<*>, R : GropiusRepository<T, String>>(
    repository: R
) : BasePermissionService<T, R>(repository) {

    /**
     * Creates the default permission on node creation
     * The created permission grants ADMIN and READ only to the provided [user]
     * Does not check permissions or save the created permission
     *
     * @param user the user who is granted the permission
     * @param constructor used to create the Permission instance
     * @return the created permission
     */
    protected suspend fun createDefaultPermission(
        user: GropiusUser, constructor: (String, String, MutableList<String>, Boolean) -> T
    ): T {
        val permission = constructor(
            "Admin permission",
            "The admin permission generated on node creation",
            mutableListOf(NodePermission.READ, NodePermission.ADMIN),
            false
        )
        permission.users() += user
        return permission
    }

}