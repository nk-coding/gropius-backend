package gropius.service.architecture

import gropius.model.architecture.*
import gropius.model.template.ComponentTemplate
import gropius.model.template.InterfaceSpecificationInheritanceCondition
import gropius.model.template.InterfaceSpecificationTemplate
import io.github.graphglue.model.Node
import io.github.graphglue.model.property.NodeCache

/**
 * Helper class to handle anything InterfaceSpecification derivation related
 *
 * After using, nodes from [deletedNodes] must be deleted, and nodes from [updatedNodes] must be saved
 *
 */
class ComponentGraphUpdater {
    /**
     * Cache used to ensure that only one instance of each Node is loaded by the algorithm
     */
    private val cache = NodeCache()

    /**
     * Nodes which should be deleted
     */
    val deletedNodes = mutableSetOf<Node>()

    /**
     * Nodes which are updated and need to be saved
     * This may contain nodes also present in [deletedNodes]
     */
    private val internalUpdatedNodes: MutableSet<Node> = mutableSetOf()

    /**
     * Nodes which are updated and need to be saved
     * Does not include any deleted nodes
     */
    val updatedNodes: Set<Node> get() = internalUpdatedNodes - (deletedNodes)

    /**
     * Called when a [Component] should be deleted
     *
     * Handles the deletion of []
     */
    suspend fun deleteComponent(component: Component) {
        cache.add(component)
        deletedNodes += component
        component.interfaceSpecifications(cache).forEach {
            deleteInterfaceSpecification(it)
        }
        component.versions(cache).forEach {
            deleteComponentVersion(it)
        }
    }

    suspend fun deleteComponentVersion(componentVersion: ComponentVersion) {
        cache.add(componentVersion)
        deletedNodes += componentVersion
        componentVersion.outgoingRelations(cache).forEach {
            deleteRelation(it)
        }
        componentVersion.incomingRelations(cache).forEach {
            deleteRelation(it)
        }
    }

    suspend fun deleteRelation(relation: Relation) {
        if (relation !in deletedNodes) {
            deletedNodes += relation
            relation.start(cache).value.outgoingRelations(cache) -= relation
            val endNode = relation.end(cache).value
            endNode.incomingRelations(cache) -= relation
            relation.derivesVisible(cache).forEach {
                it.visibleDerivedBy(cache) -= relation
            }
            relation.derivesInvisible(cache).forEach {
                it.invisibleDerivedBy(cache) -= relation
            }
            if (endNode is ComponentVersion) {
                validateComponentVersion(endNode)
            }
        }
    }

    suspend fun deleteInterfaceSpecification(interfaceSpecification: InterfaceSpecification) {
        cache.add(interfaceSpecification)
        deletedNodes += interfaceSpecification
        deletedNodes += interfaceSpecification.definedParts(cache)
        interfaceSpecification.versions(cache).forEach {
            deleteInterfaceSpecificationVersion(it)
        }
    }

    suspend fun deleteInterfaceSpecificationVersion(interfaceSpecificationVersion: InterfaceSpecificationVersion) {
        cache.add(interfaceSpecificationVersion)
        deletedNodes += interfaceSpecificationVersion
        interfaceSpecificationVersion.definitions(cache).toSet().forEach {
            deleteInterfaceDefinition(it)
        }
    }

    suspend fun updateComponentTemplate(component: Component) {
        component.versions(cache).forEach { componentVersion ->
            val updatedDefinitions = componentVersion.incomingRelations(cache).flatMap { relation ->
                addForUpdatedRelation(relation)
            }.toSet()
            addForUpdatedComponentVersion(componentVersion, updatedDefinitions)
        }
        val template = component.template(cache).value
        component.versions(cache).forEach { componentVersion ->
            val updatedInterfaceSpecificationVersions = mutableSetOf<InterfaceSpecificationVersion>()
            componentVersion.interfaceDefinitions(cache).toSet().forEach {
                if (validateInterfaceDefinitionAfterTemplateUpdate(it, template)) {
                    updatedInterfaceSpecificationVersions += it.interfaceSpecificationVersion(cache).value
                }
            }
            validateRelatedComponentVersions(componentVersion, updatedInterfaceSpecificationVersions)
        }
    }

    suspend fun updateInterfaceSpecificationTemplate(interfaceSpecification: InterfaceSpecification) {
        val definitions = interfaceSpecification.versions(cache).flatMap { it.definitions(cache) }
        for (definition in definitions) {
            val componentVersion = definition.componentVersion(cache).value
            val template = componentVersion.component(cache).value.template(cache).value
            if (validateInterfaceDefinitionAfterTemplateUpdate(definition, template)) {
                validateRelatedComponentVersions(
                    componentVersion, setOf(definition.interfaceSpecificationVersion(cache).value)
                )
            }
        }
    }

    suspend fun removeInterfaceSpecificationVersionFromComponentVersion(
        interfaceSpecificationVersion: InterfaceSpecificationVersion,
        componentVersion: ComponentVersion,
        visible: Boolean,
        invisible: Boolean
    ) {
        val definition = componentVersion.interfaceDefinitions(cache).firstOrNull {
            it.interfaceSpecificationVersion(cache).value == interfaceSpecificationVersion
        }
        if (definition != null) {
            if (visible) {
                definition.visibleSelfDefined = false
            }
            if (invisible) {
                definition.invisibleSelfDefined = false
            }
            internalUpdatedNodes += definition
            validateRelatedComponentVersions(componentVersion, setOf(interfaceSpecificationVersion))
        }

    }

    suspend fun addInterfaceSpecificationVersionToComponentVersion(
        interfaceSpecificationVersion: InterfaceSpecificationVersion,
        componentVersion: ComponentVersion,
        visible: Boolean,
        invisible: Boolean
    ) {
        val definition = getOrCreateInterfaceDefinition(componentVersion, interfaceSpecificationVersion)
        if (visible) {
            definition.visibleSelfDefined = true
        }
        if (invisible) {
            definition.invisibleSelfDefined = true
        }
        internalUpdatedNodes += definition
        addForUpdatedComponentVersion(componentVersion, setOf(definition))
    }

    suspend fun createRelation(relation: Relation) {
        updatedRelation(relation)
    }

    suspend fun updateRelationTemplate(relation: Relation) {
        updatedRelation(relation)
        val endNode = relation.end(cache).value
        if (endNode is ComponentVersion) {
            validateComponentVersion(endNode)
        }
    }

    /**
     * Called after the template of a [Component] or an [InterfaceSpecification] was updated
     * Validates that [interfaceDefinition] can still be present on the associated [ComponentVersion],
     * and if not, removes it (maybe partially)
     * Does NOT perform any validation of the graph
     *
     * @param interfaceDefinition the [InterfaceDefinition] to validate
     * @param template the template of the [Component] associated with the [ComponentVersion] associated with [interfaceDefinition]
     * @return `true` if the [interfaceDefinition] was updated, and a graph update should be triggered
     */
    private suspend fun validateInterfaceDefinitionAfterTemplateUpdate(
        interfaceDefinition: InterfaceDefinition, template: ComponentTemplate
    ): Boolean {
        val interfaceSpecificationVersion = interfaceDefinition.interfaceSpecificationVersion(cache).value
        val interfaceSpecification = interfaceSpecificationVersion.interfaceSpecification(cache).value
        val specificationTemplate = interfaceSpecification.template(cache).value
        var updated = false
        if (template !in specificationTemplate.canBeVisibleOnComponents(cache)) {
            updated = true
            interfaceDefinition.visibleSelfDefined = false
            interfaceDefinition.visibleDerivedBy(cache).clear()
        }
        if (template !in specificationTemplate.canBeInvisibleOnComponents(cache)) {
            updated = true
            interfaceDefinition.invisibleSelfDefined = false
            interfaceDefinition.invisibleDerivedBy(cache).clear()
        }
        if (updated) {
            handleUpdatedInterfaceDefinition(interfaceDefinition)
        }
        return updated
    }

    private suspend fun updatedRelation(relation: Relation) {
        val updatedDefinitions = addForUpdatedRelation(relation)
        val endNode = relation.end(cache).value
        if (endNode is ComponentVersion) {
            addForUpdatedComponentVersion(endNode, updatedDefinitions)
        }
    }

    private suspend fun addForUpdatedRelation(
        relation: Relation, startDefinitions: Set<InterfaceDefinition>? = null
    ): Set<InterfaceDefinition> {
        if (relation !in deletedNodes) {
            val startNode = relation.start(cache).value
            val endNode = relation.end(cache).value
            if (startNode !is ComponentVersion || endNode !is ComponentVersion) {
                return emptySet()
            }
            val startTemplate = startNode.template(cache).value.partOf(cache).value
            val endTemplate = endNode.template(cache).value.partOf(cache).value
            val inheritanceConditionsByTemplate =
                mutableMapOf<InterfaceSpecificationTemplate, MutableList<InterfaceSpecificationInheritanceCondition>>()
            relation.template(cache).value.relationConditions(cache).forEach { relationCondition ->
                if (startTemplate in relationCondition.from(cache) && endTemplate in relationCondition.to(cache)) {
                    relationCondition.interfaceSpecificationInheritanceConditions(cache).forEach { condition ->
                        condition.inheritableInterfaceSpecifications(cache).forEach {
                            inheritanceConditionsByTemplate.computeIfAbsent(it) {
                                mutableListOf()
                            }.add(condition)
                        }
                    }
                }
            }
            val updatedDefinitions = mutableSetOf<InterfaceDefinition>()
            for (definition in startDefinitions ?: startNode.interfaceDefinitions(cache)) {
                val interfaceSpecificationVersion = definition.interfaceSpecificationVersion(cache).value
                val interfaceSpecification = interfaceSpecificationVersion.interfaceSpecification(cache).value
                val interfaceSpecificationTemplate = interfaceSpecification.template(cache).value
                val conditions = inheritanceConditionsByTemplate[interfaceSpecificationTemplate] ?: emptySet()
                val canBeVisible = endTemplate in interfaceSpecificationTemplate.canBeVisibleOnComponents(cache)
                val canBeInvisible = endTemplate in interfaceSpecificationTemplate.canBeInvisibleOnComponents(cache)
                for (condition in conditions) {
                    if (condition.inheritsVisibleSelfDefined && definition.visibleSelfDefined || condition.inheritsInvisibleSelfDefined && definition.invisibleSelfDefined || condition.inheritsVisibleDerived && definition.visibleDerivedBy(
                            cache
                        ).isNotEmpty() || condition.inheritsInvisibleDerived && definition.invisibleDerivedBy(cache)
                            .isNotEmpty()
                    ) {
                        if (condition.isVisibleInherited && canBeVisible || condition.isInvisibleInherited && canBeInvisible) {
                            val targetDefinition = getOrCreateInterfaceDefinition(
                                startNode, interfaceSpecificationVersion
                            )
                            if (condition.isVisibleInherited) {
                                if (targetDefinition.visibleDerivedBy(cache).add(relation)) {
                                    internalUpdatedNodes += targetDefinition
                                    updatedDefinitions += targetDefinition
                                    if (!targetDefinition.visibleSelfDefined && targetDefinition.visibleDerivedBy(cache).size == 1) {
                                        handleUpdatedInterfaceDefinition(targetDefinition)
                                    }
                                }
                            }
                            if (condition.isInvisibleInherited) {
                                if (targetDefinition.invisibleDerivedBy(cache).add(relation)) {
                                    internalUpdatedNodes += targetDefinition
                                    updatedDefinitions += targetDefinition
                                }
                            }
                        }
                    }
                }
            }
            return updatedDefinitions
        } else {
            return emptySet()
        }
    }

    private suspend fun addForUpdatedComponentVersion(
        componentVersion: ComponentVersion, updatedDefinitions: Set<InterfaceDefinition>
    ) {
        val updatedComponentVersions = componentVersion.outgoingRelations(cache).flatMap {
            addForUpdatedRelation(it, updatedDefinitions)
        }.groupBy { it.componentVersion(cache).value }.mapValues { it.value.toSet() }

        updatedComponentVersions.forEach { (componentVersion, definitions) ->
            addForUpdatedComponentVersion(componentVersion, definitions)
        }
    }

    private suspend fun getOrCreateInterfaceDefinition(
        componentVersion: ComponentVersion, interfaceSpecificationVersion: InterfaceSpecificationVersion
    ): InterfaceDefinition {
        return componentVersion.interfaceDefinitions(cache).firstOrNull {
            it.interfaceSpecificationVersion(cache).value == interfaceSpecificationVersion
        } ?: run {
            val template =
                interfaceSpecificationVersion.template(cache).value.partOf(cache).value.interfaceDefinitionTemplate(
                    cache
                ).value
            val newDefinition = InterfaceDefinition(
                false, false, template.templateFieldSpecifications.mapValues { "null" }.toMutableMap()
            )
            newDefinition.template(cache).value = template
            componentVersion.interfaceDefinitions(cache) += newDefinition
            internalUpdatedNodes += componentVersion
            newDefinition
        }
    }

    private suspend fun deleteInterfaceDefinition(definition: InterfaceDefinition) {
        definition.visibleSelfDefined = false
        definition.invisibleSelfDefined = false
        definition.visibleDerivedBy(cache).clear()
        definition.invisibleDerivedBy(cache).clear()
        handleUpdatedInterfaceDefinition(definition)
    }

    private suspend fun handleUpdatedInterfaceDefinition(definition: InterfaceDefinition) {
        if (definition.visibleSelfDefined || definition.visibleDerivedBy(cache).isNotEmpty()) {
            if (definition.visibleInterface(cache).value == null) {
                val specification = definition.interfaceSpecificationVersion(cache).value
                val template = definition.template(cache).value.partOf(cache).value.interfaceTemplate(cache).value
                val newInterface = Interface(
                    specification.name,
                    specification.description,
                    template.templateFieldSpecifications.mapValues { "null" }.toMutableMap()
                )
                newInterface.template(cache).value = template
                internalUpdatedNodes += definition
                definition.visibleInterface(cache).value = newInterface
            }
        }
        if (!definition.visibleSelfDefined && definition.visibleDerivedBy(cache).isEmpty()) {
            val visibleInterface = definition.visibleInterface(cache).value
            if (visibleInterface != null) {
                deletedNodes += visibleInterface
                deletedNodes += visibleInterface.incomingRelations(cache)
                deletedNodes += visibleInterface.outgoingRelations(cache)
                definition.visibleInterface(cache).value = null
            }
            if (!definition.invisibleSelfDefined && definition.invisibleDerivedBy(cache).isEmpty()) {
                deletedNodes += definition
                definition.componentVersion(cache).value.interfaceDefinitions(cache) -= definition
                definition.interfaceSpecificationVersion(cache).value.definitions(cache) -= definition
            }
        }
    }

    private suspend fun validateRelatedComponentVersions(
        componentVersion: ComponentVersion,
        startInterfaceSpecificationVersions: Set<InterfaceSpecificationVersion>? = null
    ) {
        for (relation in componentVersion.outgoingRelations(cache)) {
            val endNode = relation.end(cache).value
            if (endNode is ComponentVersion) {
                if (startInterfaceSpecificationVersions != null) {
                    validateComponentVersion(endNode, startInterfaceSpecificationVersions)
                } else {
                    validateComponentVersion(componentVersion)
                }
            }
        }
    }

    private suspend fun validateComponentVersion(
        componentVersion: ComponentVersion,
        startInterfaceSpecificationVersions: Set<InterfaceSpecificationVersion>? = null
    ) {
        if (startInterfaceSpecificationVersions != null) {
            validateComponentVersionByInterfaceDefinitions(componentVersion,
                componentVersion.interfaceDefinitions(cache)
                    .filter { it.interfaceSpecificationVersion(cache).value in startInterfaceSpecificationVersions }
                    .toSet())
        } else {
            validateComponentVersionByInterfaceDefinitions(componentVersion)
        }

    }

    private suspend fun validateComponentVersionByInterfaceDefinitions(
        componentVersion: ComponentVersion, startDefinitions: Set<InterfaceDefinition>? = null
    ) {
        val nextInterfaceSpecificationVersions = mutableSetOf<InterfaceSpecificationVersion>()
        for (definition in startDefinitions ?: componentVersion.interfaceDefinitions(cache).toSet()) {
            val circularDerivationChecker = CircularDerivationChecker(definition)
            var updated = false
            if (!circularDerivationChecker.checkIsStillDerived(true)) {
                definition.visibleDerivedBy(cache).clear()
                updated = true
            }
            if (!circularDerivationChecker.checkIsStillDerived(false)) {
                definition.invisibleDerivedBy(cache).clear()
                updated = true
            }
            if (updated) {
                nextInterfaceSpecificationVersions += definition.interfaceSpecificationVersion(cache).value
                handleUpdatedInterfaceDefinition(definition)
            }
        }
        validateRelatedComponentVersions(componentVersion, nextInterfaceSpecificationVersions)
    }

    inner class CircularDerivationChecker(
        private val interfaceDefinition: InterfaceDefinition
    ) {
        private val checkedVisibleComponentVersions: MutableSet<ComponentVersion> = mutableSetOf()
        private val checkedInvisibleComponentVersions: MutableSet<ComponentVersion> = mutableSetOf()
        private lateinit var interfaceSpecificationVersion: InterfaceSpecificationVersion
        private lateinit var interfaceSpecificationTemplate: InterfaceSpecificationTemplate

        suspend fun checkIsStillDerived(visible: Boolean): Boolean {
            interfaceSpecificationVersion = interfaceDefinition.interfaceSpecificationVersion(cache).value
            interfaceSpecificationTemplate =
                interfaceSpecificationVersion.interfaceSpecification(cache).value.template(cache).value
            return if (visible) {
                checkIsDerivedVisible(interfaceDefinition)
            } else {
                checkIsDerivedInvisible(interfaceDefinition)
            }
        }

        private suspend fun checkIsDerivedVisible(definition: InterfaceDefinition): Boolean {
            val componentVersion = definition.componentVersion(cache).value
            if (componentVersion in checkedVisibleComponentVersions) {
                return false
            }
            checkedVisibleComponentVersions += componentVersion
            definition.visibleDerivedBy(cache).forEach {
                if (doesRelationStillDerive(it, true)) {
                    return true
                }
            }
            return false
        }

        private suspend fun checkIsDerivedInvisible(definition: InterfaceDefinition): Boolean {
            val componentVersion = definition.componentVersion(cache).value
            if (componentVersion in checkedInvisibleComponentVersions) {
                return false
            }
            checkedInvisibleComponentVersions += componentVersion
            definition.invisibleDerivedBy(cache).forEach {
                if (doesRelationStillDerive(it, false)) {
                    return true
                }
            }
            return false
        }

        private suspend fun doesRelationStillDerive(
            relation: Relation, derivedVisible: Boolean
        ): Boolean {
            if (relation in deletedNodes) {
                throw IllegalStateException("found deleted relation")
            }
            val startNode = relation.start(cache).value
            val endNode = relation.end(cache).value
            if (startNode !is ComponentVersion || endNode !is ComponentVersion) {
                return false
            }
            val startDefinition = startNode.interfaceDefinitions(cache).firstOrNull {
                if (it in deletedNodes) {
                    throw IllegalStateException("found deleted definition")
                }
                it.interfaceSpecificationVersion(cache).value == interfaceSpecificationVersion
            } ?: return false

            val startTemplate = startNode.template(cache).value.partOf(cache).value
            val endTemplate = endNode.template(cache).value.partOf(cache).value

            var byVisibleSelfDefined = false
            var byVisibleDerived = false
            var byInvisibleSelfDefined = false
            var byInvisibleDerived = false
            relation.template(cache).value.relationConditions(cache).forEach { relationCondition ->
                if (startTemplate in relationCondition.from(cache) && endTemplate in relationCondition.to(cache)) {
                    relationCondition.interfaceSpecificationInheritanceConditions(cache).forEach {
                        if (it.inheritableInterfaceSpecifications(cache).contains(interfaceSpecificationTemplate)) {
                            if (it.isVisibleInherited && derivedVisible || it.isInvisibleInherited && !derivedVisible) {
                                byVisibleSelfDefined = byVisibleSelfDefined || it.inheritsVisibleSelfDefined
                                byVisibleDerived = byVisibleDerived || it.inheritsVisibleDerived
                                byInvisibleSelfDefined = byInvisibleSelfDefined || it.inheritsInvisibleSelfDefined
                                byInvisibleDerived = byInvisibleDerived || it.inheritsInvisibleDerived
                            }
                        }
                    }
                }
            }

            return byVisibleSelfDefined && startDefinition.visibleSelfDefined || byInvisibleSelfDefined && startDefinition.invisibleSelfDefined || byVisibleDerived && checkIsDerivedVisible(
                startDefinition
            ) || byInvisibleDerived && checkIsDerivedInvisible(startDefinition)
        }
    }
}