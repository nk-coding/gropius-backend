package gropius.model.template

import gropius.model.architecture.InterfaceSpecification
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient


@DomainNode
class InterfaceSpecificationTemplate(
    name: String, description: String, isDeprecated: Boolean
) : RelationPartnerTemplate<InterfaceSpecification, InterfaceSpecificationTemplate>(name, description, isDeprecated) {

    @NodeRelationship(ComponentTemplate.VISIBLE_INTERFACE_SPECIFICATION, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val possibleVisibleInterfaceSpecifications by NodeSetProperty<ComponentTemplate>()

    @NodeRelationship(ComponentTemplate.INVISIBLE_INTERFACE_SPECIFICATION, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val possibleInvisibleInterfaceSpecifications by NodeSetProperty<InterfaceSpecificationInheritanceCondition>()

}