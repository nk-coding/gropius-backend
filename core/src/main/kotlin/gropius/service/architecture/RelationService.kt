package gropius.service.architecture

import com.expediagroup.graphql.generator.scalars.ID
import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.architecture.CreateRelationInput
import gropius.dto.input.architecture.UpdateRelationInput
import gropius.dto.input.common.DeleteNodeInput
import gropius.dto.input.ifPresent
import gropius.dto.input.isPresent
import gropius.model.architecture.*
import gropius.model.template.RelationTemplate
import gropius.model.user.permission.ComponentPermission
import gropius.model.user.permission.NodePermission
import gropius.repository.architecture.InterfacePartRepository
import gropius.repository.architecture.RelationPartnerRepository
import gropius.repository.architecture.RelationRepository
import gropius.repository.common.NodeRepository
import gropius.repository.findById
import gropius.repository.template.RelationTemplateRepository
import gropius.service.common.AbstractExtensibleNodeService
import gropius.service.template.TemplatedNodeService
import io.github.graphglue.authorization.Permission
import io.github.graphglue.model.Node
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

/**
 * Service for [Relation]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 * @param relationPartnerRepository used to get start and end of a Relation by id
 * @param relationTemplateRepository used to get the [RelationTemplate] by id
 * @param templatedNodeService service used to update templatedFields
 * @param interfacePartRepository used to get [InterfacePart]s by id
 * @param nodeRepository used to save/delete any node
 */
@Service
class RelationService(
    repository: RelationRepository,
    val relationPartnerRepository: RelationPartnerRepository,
    val relationTemplateRepository: RelationTemplateRepository,
    val templatedNodeService: TemplatedNodeService,
    val interfacePartRepository: InterfacePartRepository,
    val nodeRepository: NodeRepository
) : AbstractExtensibleNodeService<Relation, RelationRepository>(repository) {

    /**
     * Creates a new [Relation] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines the [Relation]
     * @return the saved created [Relation]
     */
    suspend fun createRelation(
        authorizationContext: GropiusAuthorizationContext, input: CreateRelationInput
    ): Relation {
        input.validate()
        val start = relationPartnerRepository.findById(input.start)
        val end = relationPartnerRepository.findById(input.end)
        checkPermission(
            start,
            Permission(ComponentPermission.RELATE_FROM_COMPONENT, authorizationContext),
            "use the specified start in Relations"
        )
        checkPermission(
            end,
            Permission(ComponentPermission.RELATE_TO_COMPONENT, authorizationContext),
            "use the specified end in Relations"
        )
        val template = relationTemplateRepository.findById(input.template)
        validateRelationStartAndEnd(start, end, template)
        val templatedFields = templatedNodeService.validateInitialTemplatedFields(template, input)
        val relation = Relation(templatedFields)
        relation.start().value = start
        relation.end().value = end
        input.startParts.ifPresent {
            relation.startParts() += getInterfaceParts(start, it)
        }
        input.endParts.ifPresent {
            relation.endParts() += getInterfaceParts(end, it)
        }
        relation.template().value = template
        createdExtensibleNode(relation, input)
        val graphUpdater = ComponentGraphUpdater()
        graphUpdater.createRelation(relation)
        nodeRepository.deleteAll(graphUpdater.deletedNodes)
        val updatedNodes = graphUpdater.updatedNodes + relation
        return nodeRepository.saveAll(updatedNodes).collectList().awaitSingle().first { it == relation } as Relation
    }

    /**
     * Helper used to get [InterfacePart]s based on a list of ids for a [RelationPartner]
     * The [relationPartner] must be a [Interface], if a non-empty list of ids is provided
     * Each [InterfacePart] must be active on the [InterfaceSpecificationVersion] related to the [Interface]
     *
     * @param relationPartner the start/end of the relation
     * @param partIds the ids of the [InterfacePart]s
     * @return the list of queried [InterfacePart]s from the [partIds], empty list if no ids are provided
     * @throws IllegalStateException if any validation fails
     */
    private suspend fun getInterfaceParts(
        relationPartner: RelationPartner, partIds: List<ID>
    ): List<InterfacePart> {
        if (partIds.isNotEmpty()) {
            if (relationPartner !is Interface) {
                throw IllegalStateException("InterfaceParts can only be provided if the side of the Relation uses an Interface")
            }
            val interfaceSpecificationVersion =
                relationPartner.interfaceDefinition().value.interfaceSpecificationVersion().value
            val parts = partIds.map { interfacePartRepository.findById(it) }
            parts.forEach {
                if (interfaceSpecificationVersion !in it.activeOn()) {
                    throw IllegalStateException("InterfacePart must be active on the used InterfaceSpecificationVersion")
                }
            }
            return parts
        }
        return emptyList()
    }

    /**
     * Validates that a start, end and relationPartner combination is valid
     *
     * @param start the start of the Relation
     * @param end the end of the Relation
     * @param template the template of the Relation
     * @throws IllegalStateException if the combination is invalid
     */
    private suspend fun validateRelationStartAndEnd(
        start: RelationPartner, end: RelationPartner, template: RelationTemplate
    ) {
        val startTemplate = start.template().value
        val endTemplate = end.template().value
        if (template.relationConditions().none { startTemplate in it.from() && endTemplate in it.to() }) {
            throw IllegalStateException("No RelationCondition allows the chosen relationTemplate & start & end combination")
        }
    }

    /**
     * Updates a [Relation] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines which [Relation] to update and how
     * @return the updated [Relation]
     */
    suspend fun updateRelation(
        authorizationContext: GropiusAuthorizationContext, input: UpdateRelationInput
    ): Relation {
        input.validate()
        val relation = repository.findById(input.id)
        checkPermission(
            relation.start().value,
            Permission(ComponentPermission.RELATE_FROM_COMPONENT, authorizationContext),
            "update the Relation due to missing permission on start"
        )
        checkPermission(
            relation.end().value,
            Permission(ComponentPermission.RELATE_TO_COMPONENT, authorizationContext),
            "update the Relation due to missing permission on end"
        )
        val nodesToSave = mutableSetOf<Node>(relation)
        input.template.ifPresent { templateId ->
            val template = relationTemplateRepository.findById(templateId)
            validateRelationStartAndEnd(relation.start().value, relation.end().value, template)
            relation.template().value = template
            val graphUpdater = ComponentGraphUpdater()
            graphUpdater.updateRelationTemplate(relation)
            nodeRepository.deleteAll(graphUpdater.deletedNodes)
            nodesToSave += graphUpdater.updatedNodes
        }
        input.addedStartParts.ifPresent {
            relation.startParts() += getInterfaceParts(relation.start().value, it)
        }
        input.removedStartParts.ifPresent {
            relation.startParts() -= getInterfaceParts(relation.start().value, it).toSet()
        }
        input.addedEndParts.ifPresent {
            relation.endParts() += getInterfaceParts(relation.end().value, it)
        }
        input.removedEndParts.ifPresent {
            relation.endParts() -= getInterfaceParts(relation.end().value, it).toSet()
        }
        templatedNodeService.updateTemplatedFields(relation, input, input.template.isPresent)
        updateExtensibleNode(relation, input)
        return nodeRepository.saveAll(nodesToSave).collectList().awaitSingle().first { it == relation } as Relation
    }

    /**
     * Deletes a [Relation] by id
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines which [Relation] to delete
     */
    suspend fun deleteRelation(
        authorizationContext: GropiusAuthorizationContext, input: DeleteNodeInput
    ) {
        input.validate()
        val relation = repository.findById(input.id)
        if (!evaluatePermission(
                relation.start().value,
                Permission(ComponentPermission.RELATE_FROM_COMPONENT, authorizationContext)
            ) && !evaluatePermission(
                relation.end().value,
                Permission(ComponentPermission.RELATE_TO_COMPONENT, authorizationContext)
            )
        ) {
            throw IllegalStateException("User does not have permission to delete the Relation")
        }
        val graphUpdater = ComponentGraphUpdater()
        graphUpdater.deleteRelation(relation)
        nodeRepository.deleteAll(graphUpdater.deletedNodes).awaitSingleOrNull()
        nodeRepository.saveAll(graphUpdater.updatedNodes).collectList().awaitSingle()
    }

}