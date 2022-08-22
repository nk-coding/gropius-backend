package gropius.service.user.permission

import gropius.model.architecture.Trackable
import gropius.model.user.permission.NodeWithPermissions
import gropius.model.user.permission.TrackablePermission
import gropius.repository.GropiusRepository

/**
 * Base class for services for subclasses of [TrackablePermission]
 *
 * @param repository the associated repository used for CRUD functionality
 * @param T the type of Node this service is used for
 * @param R Repository type associated with [T]
 */
abstract class TrackablePermissionService<T : TrackablePermission<V>, V, R : GropiusRepository<T, String>>(
    repository: R
) : NodePermissionService<T, V, R>(repository) where V : Trackable, V : NodeWithPermissions<T>