package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.architecture.InterfaceSpecification
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode("interfaceSpecificationTemplates")
@GraphQLDescription(
    """Template for InterfaceSpecifications.
    Defines templated fields with specific types (defined using JSON schema).
    """
)
class InterfaceSpecificationTemplate(
    name: String, description: String, isDeprecated: Boolean
) : RelationPartnerTemplate<InterfaceSpecification, InterfaceSpecificationTemplate>(name, description, isDeprecated) {

    @NodeRelationship(ComponentTemplate.VISIBLE_INTERFACE_SPECIFICATION, Direction.INCOMING)
    @GraphQLDescription("Templates of Components InterfaceSpecifications with this template can be visible on.")
    @FilterProperty
    @delegate:Transient
    val canBeVisibleOnComponents by NodeSetProperty<ComponentTemplate>()

    @NodeRelationship(ComponentTemplate.INVISIBLE_INTERFACE_SPECIFICATION, Direction.INCOMING)
    @GraphQLDescription("Templates of Components InterfaceSpecifications with this template can be invisible on.")
    @FilterProperty
    @delegate:Transient
    val canBeInvisibleOnComponents by NodeSetProperty<ComponentTemplate>()

    @NodeRelationship(
        InterfaceSpecificationInheritanceCondition.INHERITABLE_INTERFACE_SPECIFICATION, Direction.INCOMING
    )
    @GraphQLDescription(
        """InterfaceSpecificationInheritanceConditions which allow to inherit InterfaceSpecification with this template.
        """
    )
    @FilterProperty
    @delegate:Transient
    val inheritableBy by NodeSetProperty<InterfaceSpecificationInheritanceCondition>()

}