package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.architecture.Component
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode("componentTemplates")
@GraphQLDescription(
    """Template for Components.
    Defines templated fields with specific types (defined using JSON schema).
    Defines which InterfaceSpecifications can be (in)visible on Components with this Template.
    """
)
class ComponentTemplate(
    name: String, description: String, isDeprecated: Boolean
) : RelationPartnerTemplate<Component, ComponentTemplate>(name, description, isDeprecated) {

    companion object {
        const val VISIBLE_INTERFACE_SPECIFICATION = "VISIBLE_INTERFACE_SPECIFICATION"
        const val INVISIBLE_INTERFACE_SPECIFICATION = "INVISIBLE_INTERFACE_SPECIFICATION"
    }

    @NodeRelationship(VISIBLE_INTERFACE_SPECIFICATION, Direction.OUTGOING)
    @GraphQLDescription("Templates of InterfaceSpecifications which can be visible on Components with this Template.")
    @FilterProperty
    @delegate:Transient
    val possibleVisibleInterfaceSpecifications by NodeSetProperty<InterfaceSpecificationTemplate>()

    @NodeRelationship(INVISIBLE_INTERFACE_SPECIFICATION, Direction.OUTGOING)
    @GraphQLDescription("Templates of InterfaceSpecifications which can be invisible on Components with this Template.")
    @FilterProperty
    @delegate:Transient
    val possibleInvisibleInterfaceSpecifications by NodeSetProperty<InterfaceSpecificationTemplate>()

}