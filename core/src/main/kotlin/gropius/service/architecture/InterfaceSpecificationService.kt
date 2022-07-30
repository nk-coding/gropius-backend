package gropius.service.architecture

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.architecture.CreateInterfaceSpecificationInput
import gropius.dto.input.architecture.InterfaceSpecificationInput
import gropius.dto.input.architecture.UpdateInterfaceSpecificationInput
import gropius.dto.input.common.DeleteNodeInput
import gropius.dto.input.ifPresent
import gropius.dto.input.isPresent
import gropius.model.architecture.Component
import gropius.model.architecture.InterfacePart
import gropius.model.architecture.InterfaceSpecification
import gropius.model.architecture.InterfaceSpecificationVersion
import gropius.model.template.InterfaceSpecificationTemplate
import gropius.model.user.permission.NodePermission
import gropius.repository.architecture.ComponentRepository
import gropius.repository.architecture.InterfaceSpecificationRepository
import gropius.repository.common.NodeRepository
import gropius.repository.findById
import gropius.repository.template.InterfaceSpecificationTemplateRepository
import gropius.service.template.TemplatedNodeService
import io.github.graphglue.authorization.Permission
import io.github.graphglue.model.Node
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

/**
 * Service for [InterfaceSpecification]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 * @param componentRepository used to get [Component]s
 * @param interfaceSpecificationVersionService used to create [InterfaceSpecificationVersion]s
 * @param interfacePartService used to create [InterfacePart]s
 * @param templatedNodeService used to update templatedFields
 * @param interfaceSpecificationTemplateRepository used to get [InterfaceSpecificationTemplate]s
 * @param nodeRepository used to save/delete any node
 */
@Service
class InterfaceSpecificationService(
    repository: InterfaceSpecificationRepository,
    val componentRepository: ComponentRepository,
    val interfaceSpecificationVersionService: InterfaceSpecificationVersionService,
    val interfacePartService: InterfacePartService,
    val templatedNodeService: TemplatedNodeService,
    val interfaceSpecificationTemplateRepository: InterfaceSpecificationTemplateRepository,
    val nodeRepository: NodeRepository,
) : ServiceEffectSpecificationLocationService<InterfaceSpecification, InterfaceSpecificationRepository>(repository) {

    /**
     * Creates a new [InterfaceSpecification] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines the [InterfaceSpecification]
     * @return the saved created [InterfaceSpecification]
     */
    suspend fun createInterfaceSpecification(
        authorizationContext: GropiusAuthorizationContext, input: CreateInterfaceSpecificationInput
    ): InterfaceSpecification {
        input.validate()
        val component = componentRepository.findById(input.component)
        checkPermission(
            component,
            Permission(NodePermission.ADMIN, authorizationContext),
            "create InterfaceSpecifications on the Component"
        )
        val interfaceSpecification = createInterfaceSpecification(component, input)
        return repository.save(interfaceSpecification).awaitSingle()
    }

    /**
     * Creates a new [InterfaceSpecification] based on the provided [input] on [component]
     * Does not check the authorization status, does not save the created nodes
     * Validates the [input]
     *
     * @param component the [Component] the created [InterfaceSpecification] is part of
     * @param input defines the [InterfaceSpecification]
     * @return the created [InterfaceSpecification]
     */
    suspend fun createInterfaceSpecification(
        component: Component, input: InterfaceSpecificationInput
    ): InterfaceSpecification {
        input.validate()
        val template = interfaceSpecificationTemplateRepository.findById(input.template)
        val templatedFields = templatedNodeService.validateInitialTemplatedFields(template, input)
        val interfaceSpecification = InterfaceSpecification(input.name, input.description, templatedFields)
        interfaceSpecification.template().value = template
        createdExtensibleNode(interfaceSpecification, input)
        input.versions.ifPresent { inputs ->
            interfaceSpecification.versions() += inputs.map {
                interfaceSpecificationVersionService.createInterfaceSpecificationVersion(
                    interfaceSpecification, it
                )
            }
        }
        input.definedParts.ifPresent { inputs ->
            interfaceSpecification.definedParts() += inputs.map {
                interfacePartService.createInterfacePart(
                    interfaceSpecification, it
                )
            }
        }
        return interfaceSpecification
    }

    /**
     * Updates a [InterfaceSpecification] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines which [InterfaceSpecification] to update and how
     * @return the updated [InterfaceSpecification]
     */
    suspend fun updateInterfaceSpecification(
        authorizationContext: GropiusAuthorizationContext, input: UpdateInterfaceSpecificationInput
    ): InterfaceSpecification {
        input.validate()
        val interfaceSpecification = repository.findById(input.id)
        checkPermission(
            interfaceSpecification,
            Permission(NodePermission.ADMIN, authorizationContext),
            "update the InterfaceSpecification"
        )
        val nodesToSave = mutableSetOf<Node>(interfaceSpecification)
        input.template.ifPresent { templateId ->
            val template = interfaceSpecificationTemplateRepository.findById(templateId)
            interfaceSpecification.template().value = template
            val interfaceSpecificationVersionTemplate = template.interfaceSpecificationVersionTemplate().value
            val interfaceDefinitionTemplate = template.interfaceDefinitionTemplate().value
            val interfaceTemplate = template.interfaceTemplate().value
            interfaceSpecification.versions().forEach { interfaceSpecificationVersion ->
                interfaceSpecificationVersion.template().value = interfaceSpecificationVersionTemplate
                templatedNodeService.updateTemplatedFields(
                    interfaceSpecificationVersion, input.interfaceSpecificationVersionTemplatedFields, true
                )
                interfaceSpecificationVersion.definitions().forEach {
                    it.template().value = interfaceDefinitionTemplate
                    templatedNodeService.updateTemplatedFields(it, input.interfaceDefinitionTemplatedFields, true)
                    val visibleInterface = it.visibleInterface().value
                    if (visibleInterface != null) {
                        visibleInterface.template().value = interfaceTemplate
                        templatedNodeService.updateTemplatedFields(
                            visibleInterface, input.interfaceTemplatedFields, true
                        )
                    }
                }
            }
            val interfacePartTemplate = template.interfacePartTemplate().value
            interfaceSpecification.definedParts().forEach {
                it.template().value = interfacePartTemplate
                templatedNodeService.updateTemplatedFields(it, input.interfacePartTemplatedFields, true)
            }
            val graphUpdater = ComponentGraphUpdater()
            graphUpdater.updateInterfaceSpecificationTemplate(interfaceSpecification)
            nodeRepository.deleteAll(graphUpdater.deletedNodes).awaitSingleOrNull()
            nodesToSave += graphUpdater.updatedNodes
        }
        templatedNodeService.updateTemplatedFields(interfaceSpecification, input, input.template.isPresent)
        updateNamedNode(interfaceSpecification, input)
        return nodeRepository.saveAll(nodesToSave).collectList().awaitSingle()
            .first { it == interfaceSpecification } as InterfaceSpecification
    }

    /**
     * Deletes a [InterfaceSpecification] by id
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines which [InterfaceSpecification] to delete
     */
    suspend fun deleteInterfaceSpecification(
        authorizationContext: GropiusAuthorizationContext, input: DeleteNodeInput
    ) {
        input.validate()
        val interfaceSpecification = repository.findById(input.id)
        checkPermission(
            interfaceSpecification,
            Permission(NodePermission.ADMIN, authorizationContext),
            "delete the InterfaceSpecification"
        )
        val graphUpdater = ComponentGraphUpdater()
        graphUpdater.deleteInterfaceSpecification(interfaceSpecification)
        nodeRepository.saveAll(graphUpdater.updatedNodes).collectList().awaitSingle()
        nodeRepository.deleteAll(graphUpdater.deletedNodes).awaitSingleOrNull()
        repository.delete(interfaceSpecification).awaitSingle()
    }

}