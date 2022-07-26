package gropius.service.architecture

import com.expediagroup.graphql.generator.scalars.ID
import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.architecture.AddInterfaceSpecificationVersionToComponentVersionInput
import gropius.dto.input.architecture.RemoveInterfaceSpecificationVersionFromComponentVersionInput
import gropius.model.architecture.Component
import gropius.model.architecture.ComponentVersion
import gropius.model.architecture.InterfaceSpecificationVersion
import gropius.model.user.permission.NodePermission
import gropius.repository.architecture.ComponentVersionRepository
import gropius.repository.architecture.InterfaceSpecificationVersionRepository
import gropius.repository.common.NodeRepository
import gropius.repository.findById
import io.github.graphglue.authorization.Permission
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

/**
 * Service for [ComponentVersion]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 * @param interfaceSpecificationVersionRepository used to get [InterfaceSpecificationVersion]s by id
 * @param nodeRepository used to save/delete any node
 */
@Service
class ComponentVersionService(
    repository: ComponentVersionRepository,
    val interfaceSpecificationVersionRepository: InterfaceSpecificationVersionRepository,
    val nodeRepository: NodeRepository,
) : RelationPartnerService<ComponentVersion, ComponentVersionRepository>(repository) {

    /**
     * Adds a [InterfaceSpecificationVersion] to a [ComponentVersion] as visible/invisible
     * Checks the authorization status
     * Requires that the [InterfaceSpecificationVersion] is part of the same [Component] as the [ComponentVersion]
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines how and which [InterfaceSpecificationVersion] to add to which [ComponentVersion]
     * @return the saved updated [ComponentVersion]
     */
    suspend fun addInterfaceSpecificationToComponentVersion(
        authorizationContext: GropiusAuthorizationContext,
        input: AddInterfaceSpecificationVersionToComponentVersionInput
    ): ComponentVersion {
        input.validate()
        updateInterfaceSpecificationOnComponentVersion(
            authorizationContext, input.componentVersion, input.interfaceSpecificationVersion
        ) { graphUpdater, componentVersion, interfaceSpecificationVersion ->
            graphUpdater.addInterfaceSpecificationVersionToComponentVersion(
                interfaceSpecificationVersion, componentVersion, input.visible, input.invisible
            )
        }
        return repository.findById(input.componentVersion)
    }

    /**
     * Removes a [InterfaceSpecificationVersion] from a [ComponentVersion] as visible/invisible
     * Checks the authorization status
     * Requires that the [InterfaceSpecificationVersion] is part of the same [Component] as the [ComponentVersion]
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines how and which [InterfaceSpecificationVersion] to remove from which [ComponentVersion]
     * @return the saved updated [ComponentVersion]
     */
    suspend fun removeInterfaceSpecificationFromComponentVersion(
        authorizationContext: GropiusAuthorizationContext,
        input: RemoveInterfaceSpecificationVersionFromComponentVersionInput
    ): ComponentVersion {
        input.validate()
        updateInterfaceSpecificationOnComponentVersion(
            authorizationContext, input.componentVersion, input.interfaceSpecificationVersion
        ) { graphUpdater, componentVersion, interfaceSpecificationVersion ->
            graphUpdater.removeInterfaceSpecificationVersionFromComponentVersion(
                interfaceSpecificationVersion, componentVersion, input.visible, input.invisible
            )
        }
        return repository.findById(input.componentVersion)
    }

    /**
     * Helper function for [addInterfaceSpecificationToComponentVersion] and [removeInterfaceSpecificationFromComponentVersion]
     * Validates the user has ADMIN permission on the [ComponentVersion]
     * Validates that the [ComponentVersion] is part of the same [Component] as the [InterfaceSpecificationVersion]
     * Creates a [ComponentGraphUpdater] and calls [updateFunction] with it
     * Saves the updatedNodes and deletes the deletedNodes
     *
     * @param authorizationContext used to check for the required permission
     * @param componentVersionId the id of the [ComponentVersion] to update
     * @param interfaceSpecificationId the id of the [InterfaceSpecificationVersion] to pass to [updateFunction]
     * @param updateFunction called with the [ComponentGraphUpdater], [ComponentVersion] and [InterfaceSpecificationVersion]
     */
    private suspend fun updateInterfaceSpecificationOnComponentVersion(
        authorizationContext: GropiusAuthorizationContext,
        componentVersionId: ID,
        interfaceSpecificationId: ID,
        updateFunction: suspend (ComponentGraphUpdater, ComponentVersion, InterfaceSpecificationVersion) -> Any
    ) {
        val componentVersion = repository.findById(componentVersionId)
        checkPermission(
            componentVersion,
            Permission(NodePermission.ADMIN, authorizationContext),
            "add / remove InterfaceSpecificationVersions from the ComponentVersion"
        )
        val interfaceSpecificationVersion = interfaceSpecificationVersionRepository.findById(interfaceSpecificationId)
        val component = componentVersion.component().value
        if (interfaceSpecificationVersion.interfaceSpecification().value.component().value != component) {
            throw IllegalStateException(
                "InterfaceSpecificationVersion ${interfaceSpecificationVersion.rawId} is not part of Component ${component.rawId}"
            )
        }
        val graphUpdater = ComponentGraphUpdater()
        updateFunction(graphUpdater, componentVersion, interfaceSpecificationVersion)
        nodeRepository.saveAll(graphUpdater.updatedNodes).collectList().awaitSingle()
        nodeRepository.deleteAll(graphUpdater.deletedNodes).awaitSingleOrNull()
    }

}