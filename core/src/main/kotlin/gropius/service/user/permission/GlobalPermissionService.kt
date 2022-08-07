package gropius.service.user.permission

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.common.DeleteNodeInput
import gropius.dto.input.user.permission.CreateGlobalPermissionInput
import gropius.dto.input.user.permission.UpdateBasePermissionInput
import gropius.model.user.permission.GlobalPermission
import gropius.repository.findById
import gropius.repository.user.permission.GlobalPermissionRepository
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

/**
 * Service for [GlobalPermission]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class GlobalPermissionService(
    repository: GlobalPermissionRepository
) : BasePermissionService<GlobalPermission, GlobalPermissionRepository>(repository) {

    /**
     * Creates a subtype of [GlobalPermission]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines the created permission
     * @return the created permission
     */
    suspend fun createGlobalPermission(
        authorizationContext: GropiusAuthorizationContext, input: CreateGlobalPermissionInput
    ): GlobalPermission {
        input.validate()
        checkIsAdmin(authorizationContext, "create GlobalPermissions")
        val permission = GlobalPermission(
            input.name, input.description, input.entries.distinct().toMutableList(), input.allUsers
        )
        createdBasePermissionNode(permission, input)
        return repository.save(permission).awaitSingle()
    }

    /**
     * Updates a [GlobalPermission] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines which [GlobalPermission] to update and how
     * @return the updated [GlobalPermission]
     */
    suspend fun updateGlobalPermission(
        authorizationContext: GropiusAuthorizationContext, input: UpdateBasePermissionInput
    ): GlobalPermission {
        input.validate()
        checkIsAdmin(authorizationContext, "update GlobalPermissions")
        val permission = repository.findById(input.id)
        updateBasePermission(permission, input)
        return repository.save(permission).awaitSingle()
    }

    /**
     * Deletes a [GlobalPermission] by id
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines which [GlobalPermission] to delete
     */
    suspend fun deleteGlobalPermission(
        authorizationContext: GropiusAuthorizationContext, input: DeleteNodeInput
    ) {
        input.validate()
        checkIsAdmin(authorizationContext, "delete GlobalPermissions")
        val permission = repository.findById(input.id)
        repository.delete(permission).awaitSingleOrNull()
    }

}