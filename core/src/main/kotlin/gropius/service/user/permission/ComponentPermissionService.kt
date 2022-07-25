package gropius.service.user.permission

import gropius.model.architecture.Component
import gropius.model.user.GropiusUser
import gropius.model.user.permission.ComponentPermission
import gropius.repository.user.permission.ComponentPermissionRepository
import org.springframework.stereotype.Service

/**
 * Service for [ComponentPermission]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class ComponentPermissionService(repository: ComponentPermissionRepository) :
    TrackablePermissionService<ComponentPermission, ComponentPermissionRepository>(repository) {

    /**
     * Creates the default [ComponentPermission]
     * The created permission grants ADMIN and READ only to the provided [user]
     * Does not check permissions or save the created permission / [component]
     *
     * @param user the user who is granted the permission
     * @param component used to add the permission to
     */
    suspend fun createDefaultPermission(
        user: GropiusUser, component: Component
    ) {
        component.permissions() += createDefaultPermission(user, ::ComponentPermission)
    }

}