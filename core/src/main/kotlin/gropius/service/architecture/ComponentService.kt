package gropius.service.architecture

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.architecture.CreateComponentInput
import gropius.dto.input.architecture.UpdateComponentInput
import gropius.dto.input.common.DeleteNodeInput
import gropius.dto.input.ifPresent
import gropius.dto.input.isPresent
import gropius.model.architecture.Component
import gropius.model.template.ComponentTemplate
import gropius.model.user.permission.GlobalPermission
import gropius.model.user.permission.NodePermission
import gropius.repository.architecture.ComponentRepository
import gropius.repository.common.NodeRepository
import gropius.repository.findById
import gropius.repository.template.ComponentTemplateRepository
import gropius.service.template.TemplatedNodeService
import io.github.graphglue.authorization.Permission
import io.github.graphglue.model.Node
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

/**
 * Service for [Component]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 * @param templatedNodeService service used to update templatedFields
 * @param componentTemplateRepository used to get [ComponentTemplate]
 * @param nodeRepository used to save any node
 */
@Service
class ComponentService(
    repository: ComponentRepository,
    val templatedNodeService: TemplatedNodeService,
    val componentTemplateRepository: ComponentTemplateRepository,
    val nodeRepository: NodeRepository
) :
    TrackableService<Component, ComponentRepository>(repository) {

    /**
     * Creates a new [Component] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines the [Component]
     * @return the saved created [Component]
     */
    suspend fun createComponent(
        authorizationContext: GropiusAuthorizationContext,
        input: CreateComponentInput
    ): Component {
        input.validate()
        val user = getUser(authorizationContext)
        checkPermission(
            user,
            Permission(GlobalPermission.CAN_CREATE_COMPONENTS, authorizationContext),
            "User does not have permission to create Components"
        )
        val template = componentTemplateRepository.findById(input.template)
        val templatedFields = templatedNodeService.validateInitialTemplatedFields(template, input)
        val component = Component(input.name, input.description, input.repositoryURL, templatedFields)
        component.template().value = template
        createdExtensibleNode(component, input)
        return repository.save(component).awaitSingle()
    }

    /**
     * Updates a [Component] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines which [Component] to update and how
     * @return the updated [Component]
     */
    suspend fun updateComponent(
        authorizationContext: GropiusAuthorizationContext,
        input: UpdateComponentInput
    ): Component {
        input.validate()
        val component = repository.findById(input.id)
        val nodesToSave = mutableSetOf<Node>(component)
        checkPermission(
            component,
            Permission(NodePermission.ADMIN, authorizationContext),
            "User does not have permission to update the Component"
        )
        input.template.ifPresent {
            component.template().value = componentTemplateRepository.findById(it)
            val graphUpdater = ComponentGraphUpdater()
            graphUpdater.updateComponentTemplate(component)
            nodeRepository.deleteAll(graphUpdater.deletedNodes)
            nodesToSave += graphUpdater.updatedNodes
        }
        templatedNodeService.updateTemplatedFields(component, input, input.template.isPresent)
        updateTrackable(component, input)
        return nodeRepository.saveAll(nodesToSave).collectList().awaitSingle().first { it == component } as Component
    }

    /**
     * Deletes a [Component] by id
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines which [Component] to delete
     */
    suspend fun deleteComponent(
        authorizationContext: GropiusAuthorizationContext,
        input: DeleteNodeInput
    ) {
        input.validate()
        val component = repository.findById(input.id)
        checkPermission(
            component,
            Permission(NodePermission.ADMIN, authorizationContext),
            "User does not have permission to delete the Component"
        )
        val graphUpdater = ComponentGraphUpdater()
        graphUpdater.updateComponentTemplate(component)
        beforeDeleteTrackable(component)
        nodeRepository.deleteAll(graphUpdater.deletedNodes).awaitSingleOrNull()
        nodeRepository.saveAll(graphUpdater.updatedNodes).collectList().awaitSingle()
    }

}