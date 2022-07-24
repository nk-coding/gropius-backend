package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.common.ExtensibleNode
import gropius.model.user.permission.NodePermission
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription(
    """Defines which InterfaceSpecifications are inherited under which conditions by a Relation.
    Part of a RelationCondition, which is part of RelationTemplates.
    READ is always granted.
    """
)
@Authorization(NodePermission.READ, allowAll = true)
class InterfaceSpecificationInheritanceCondition(
    @property:GraphQLDescription("If true, visible self-defined InterfaceSpecifications are inherited")
    @FilterProperty
    val inheritsVisibleSelfDefined: Boolean,
    @property:GraphQLDescription("If true, invisible self-defined InterfaceSpecifications are inherited")
    @FilterProperty
    val inheritsInvisibleSelfDefined: Boolean,
    @property:GraphQLDescription("If true, visible derived InterfaceSpecifications are inherited")
    @FilterProperty
    val inheritsVisibleDerived: Boolean,
    @property:GraphQLDescription("If true, invisible derived InterfaceSpecifications are inherited")
    @FilterProperty
    val inheritsInvisibleDerived: Boolean,
    @property:GraphQLDescription("If true InterfaceSpecifications are visible inherited")
    @FilterProperty
    val isVisibleInherited: Boolean,
    @property:GraphQLDescription("If true InterfaceSpecifications are invisible inherited")
    @FilterProperty
    val isInvisibleInherited: Boolean
) : ExtensibleNode() {

    companion object {
        const val PART_OF = "PART_OF"
        const val INHERITABLE_INTERFACE_SPECIFICATION = "INHERITABLE_INTERFACE_SPECIFICATION"
    }

    @NodeRelationship(PART_OF, Direction.OUTGOING)
    @GraphQLDescription("The RelationCondition this is part of.")
    @FilterProperty
    @delegate:Transient
    val partOf by NodeProperty<RelationCondition>()

    @NodeRelationship(INHERITABLE_INTERFACE_SPECIFICATION, Direction.OUTGOING)
    @GraphQLDescription("Templates of InterfaceSpecifications which are inherited.")
    @FilterProperty
    @delegate:Transient
    val inheritableInterfaceSpecifications by NodeSetProperty<InterfaceSpecificationTemplate>()
}