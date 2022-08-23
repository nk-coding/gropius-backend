package gropius.service.user

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.ifPresent
import gropius.dto.input.user.UpdateGropiusUserInput
import gropius.model.user.GropiusUser
import gropius.repository.findById
import gropius.repository.user.GropiusUserRepository
import gropius.service.common.AbstractExtensibleNodeService
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

/**
 * Service for [GropiusUser]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class GropiusUserService(
    repository: GropiusUserRepository
) : AbstractExtensibleNodeService<GropiusUser, GropiusUserRepository>(repository) {

    /**
     * Updates a [GropiusUser] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines which [GropiusUser] to update and how
     * @return the updated [GropiusUser]
     */
    suspend fun updateGropiusUser(
        authorizationContext: GropiusAuthorizationContext, input: UpdateGropiusUserInput
    ): GropiusUser {
        input.validate()
        val gropiusUser = repository.findById(input.id)
        if (authorizationContext.userId != gropiusUser.rawId) {
            checkIsAdmin(authorizationContext, "update this GropiusUser")
        }
        input.displayName.ifPresent {
            gropiusUser.displayName = it
        }
        input.email.ifPresent {
            gropiusUser.email = it
        }
        input.isAdmin.ifPresent {
            checkIsAdmin(authorizationContext, "update isAdmin of a GropiusUser")
            gropiusUser.isAdmin = it
        }
        updateExtensibleNode(gropiusUser, input)
        return repository.save(gropiusUser).awaitSingle()
    }

}