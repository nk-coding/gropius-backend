package gropius.service.user.permission

import gropius.model.architecture.IMS
import gropius.model.user.GropiusUser
import gropius.model.user.permission.IMSPermission
import gropius.repository.user.permission.IMSPermissionRepository
import org.springframework.stereotype.Service

/**
 * Service for [IMSPermission]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class IMSPermissionService(repository: IMSPermissionRepository) :
    NodePermissionService<IMSPermission, IMSPermissionRepository>(repository) {

    /**
     * Creates the default [IMSPermission]
     * The created permission grants ADMIN and READ only to the provided [user]
     * Does not check permissions or save the created permission / [ims]
     *
     * @param user the user who is granted the permission
     * @param ims used to add the permission to
     */
    suspend fun createDefaultPermission(
        user: GropiusUser, ims: IMS
    ) {
        ims.permissions() += createDefaultPermission(user, ::IMSPermission)
    }

}