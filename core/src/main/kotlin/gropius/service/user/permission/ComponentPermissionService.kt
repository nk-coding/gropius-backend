package gropius.service.user.permission

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.user.permission.CreateComponentPermissionInput
import gropius.model.architecture.Component
import gropius.model.user.GropiusUser
import gropius.model.user.permission.ComponentPermission
import gropius.repository.architecture.ComponentRepository
import gropius.repository.findAllById
import gropius.repository.user.permission.ComponentPermissionRepository
import org.springframework.stereotype.Service

/**
 * Service for [ComponentPermission]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 * @param componentRepository used to get [Component]s by id
 */
@Service
class ComponentPermissionService(
    repository: ComponentPermissionRepository, private val componentRepository: ComponentRepository
) : TrackablePermissionService<ComponentPermission, Component, ComponentPermissionRepository>(repository) {

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

    /**
     * Creates a new [ComponentPermission] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines the [ComponentPermission]
     * @return the saved created [ComponentPermission]
     */
    suspend fun createComponentPermission(
        authorizationContext: GropiusAuthorizationContext, input: CreateComponentPermissionInput
    ): ComponentPermission {
        return createNodePermission(
            authorizationContext,
            input,
            componentRepository.findAllById(input.nodesWithPermission),
            ::ComponentPermission
        )
    }

}