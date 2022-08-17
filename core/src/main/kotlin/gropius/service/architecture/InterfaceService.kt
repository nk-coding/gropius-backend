package gropius.service.architecture

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.architecture.UpdateInterfaceInput
import gropius.model.architecture.Interface
import gropius.model.user.permission.NodePermission
import gropius.repository.architecture.InterfaceRepository
import gropius.repository.findById
import gropius.service.template.TemplatedNodeService
import io.github.graphglue.authorization.Permission
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

/**
 * Service for [Interface]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class InterfaceService(
    repository: InterfaceRepository, private val templatedNodeService: TemplatedNodeService
) : RelationPartnerService<Interface, InterfaceRepository>(repository) {

    /**
     * Updates a [Interface] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines which [Interface] to update and how
     * @return the updated [Interface]
     */
    suspend fun updateInterface(
        authorizationContext: GropiusAuthorizationContext, input: UpdateInterfaceInput
    ): Interface {
        input.validate()
        val interfaceToUpdate = repository.findById(input.id)
        checkPermission(
            interfaceToUpdate,
            Permission(NodePermission.ADMIN, authorizationContext),
            "update the Interface"
        )
        updateNamedNode(interfaceToUpdate, input)
        templatedNodeService.updateTemplatedFields(interfaceToUpdate, input)
        return repository.save(interfaceToUpdate).awaitSingle()
    }

}