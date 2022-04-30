package gropius.model.template

import gropius.model.architecture.Component
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient


@DomainNode
class ComponentTemplate(
    name: String, description: String, isDeprecated: Boolean
) : RelationPartnerTemplate<Component, ComponentTemplate>(name, description, isDeprecated) {

    companion object {
        const val VISIBLE_INTERFACE_SPECIFICATION = "VISIBLE_INTERFACE_SPECIFICATION"
        const val INVISIBLE_INTERFACE_SPECIFICATION = "INVISIBLE_INTERFACE_SPECIFICATION"
    }

    @NodeRelationship(VISIBLE_INTERFACE_SPECIFICATION, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val possibleVisibleInterfaceSpecifications by NodeSetProperty<InterfaceSpecificationTemplate>()

    @NodeRelationship(INVISIBLE_INTERFACE_SPECIFICATION, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val possibleInvisibleInterfaceSpecifications by NodeSetProperty<InterfaceSpecificationTemplate>()

}