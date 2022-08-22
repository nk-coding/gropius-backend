package gropius.service.user.permission

import gropius.model.user.permission.BasePermission
import gropius.service.common.NodeService
import gropius.repository.GropiusRepository

/**
 * Base class for services for subclasses of [BasePermission]
 *
 * @param repository the associated repository used for CRUD functionality
 * @param T the type of Node this service is used for
 * @param R Repository type associated with [T]
 */
abstract class BasePermissionService<T : BasePermission, R : GropiusRepository<T, String>>(
    repository: R
) : NodeService<T, R>(repository)