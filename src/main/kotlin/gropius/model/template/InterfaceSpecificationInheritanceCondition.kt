package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.common.ExtensibleNode
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription(
    """Defines which InterfaceSpecifications are inherited under which conditions by a Relation.
    Part of a RelationCondition, which is part of RelationTemplates.
    """
)
class InterfaceSpecificationInheritanceCondition(
    @GraphQLDescription("If true, visible self-defined InterfaceSpecifications are inherited")
    @FilterProperty
    val inheritsVisibleSelfDefined: Boolean,
    @GraphQLDescription("If true, invisible self-defined InterfaceSpecifications are inherited")
    @FilterProperty
    val inheritsInvisibleSelfDefined: Boolean,
    @GraphQLDescription("If true, visible derived InterfaceSpecifications are inherited")
    @FilterProperty
    val inheritsVisibleDerived: Boolean,
    @GraphQLDescription("If true, invisible derived InterfaceSpecifications are inherited")
    @FilterProperty
    val inheritsInvisibleDerived: Boolean,
    @GraphQLDescription(
        """If true, inherited InterfaceSpecifications are visible
         on the end of the Relation, otherwise invisible
        """
    )
    @FilterProperty
    val isVisibleInherited: Boolean
) : ExtensibleNode() {

    companion object {
        const val PART_OF = "PART_OF"
        const val INHERITABLE_INTERFACE_SPECIFICATION = "INHERITABLE_INTERFACE_SPECIFICATION"
    }

    @NodeRelationship(PART_OF, Direction.OUTGOING)
    @GraphQLDescription("The RelationCondition this is part of.")
    @FilterProperty
    @delegate:Transient
    var partOf by NodeProperty<RelationCondition>()

    @NodeRelationship(INHERITABLE_INTERFACE_SPECIFICATION, Direction.OUTGOING)
    @GraphQLDescription("Templates of InterfaceSpecifications which are inherited.")
    @FilterProperty
    @delegate:Transient
    val inheritableInterfaceSpecifications by NodeSetProperty<InterfaceSpecificationTemplate>()
}