package gropius.service.common

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.common.UpdateExtensionFieldsInput
import gropius.model.common.ExtensibleNode
import gropius.model.user.permission.NodePermission
import gropius.repository.common.ExtensibleNodeRepository
import gropius.repository.findById
import io.github.graphglue.authorization.Permission
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

/**
 * Service [ExtensibleNode]s. Provides functions to update
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class ExtensibleNodeService(repository: ExtensibleNodeRepository) :
    AbstractExtensibleNodeService<ExtensibleNode, ExtensibleNodeRepository>(repository) {

    /**
     * Gets the [ExtensibleNode] based on the provided [input] and updates the extensionFields accordingly
     * Checks for authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input specifies which ExtensibleNode to update
     * @return the updated saved [ExtensibleNode]
     */
    suspend fun updateExtensionFields(
        authorizationContext: GropiusAuthorizationContext,
        input: UpdateExtensionFieldsInput
    ): ExtensibleNode {
        input.validate()
        val extensibleNode = repository.findById(input.id)
        checkPermission(
            extensibleNode,
            Permission(NodePermission.READ, authorizationContext),
            "Cannot update ExtensibleNode"
        )
        updateExtensibleNode(extensibleNode, input)
        return repository.save(extensibleNode).awaitSingle()
    }
}