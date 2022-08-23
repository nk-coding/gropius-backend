package gropius.service.user.permission

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.user.permission.CreateIMSPermissionInput
import gropius.model.architecture.IMS
import gropius.model.user.GropiusUser
import gropius.model.user.permission.IMSPermission
import gropius.repository.architecture.IMSRepository
import gropius.repository.findAllById
import gropius.repository.user.permission.IMSPermissionRepository
import org.springframework.stereotype.Service

/**
 * Service for [IMSPermission]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 * @param imsRepository used for getting [IMS]s by id
 */
@Service
class IMSPermissionService(
    repository: IMSPermissionRepository, private val imsRepository: IMSRepository
) : NodePermissionService<IMSPermission, IMS, IMSPermissionRepository>(repository) {

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

    /**
     * Creates a new [IMSPermission] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines the [IMSPermission]
     * @return the saved created [IMSPermission]
     */
    suspend fun createIMSPermission(
        authorizationContext: GropiusAuthorizationContext, input: CreateIMSPermissionInput
    ): IMSPermission {
        return createNodePermission(
            authorizationContext,
            input,
            imsRepository.findAllById(input.nodesWithPermission),
            ::IMSPermission
        )
    }

}