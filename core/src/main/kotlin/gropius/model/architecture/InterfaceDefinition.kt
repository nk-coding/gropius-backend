package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.common.ExtensibleNode
import gropius.model.user.permission.NodePermission
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription(
    """InterfaceSpecificationVersion on a ComponentVersion
    Specifies if it is visible/invisible self-defined.
    Specifies if it is visible/invisible derived (and by which Relations)
    READ is granted if READ is granted on `componentVersion`
    """
)
@Authorization(NodePermission.READ, allowFromRelated = ["componentVersion"])
class InterfaceDefinition(
    @property:GraphQLDescription(
        """If true, `interfaceSpecificationVersion`is self-defined visible on the `componentVersion`"""
    )
    @FilterProperty
    @OrderProperty
    var visibleSelfDefined: Boolean,
    @property:GraphQLDescription(
        """If true, `interfaceSpecificationVersion`is self-defined invisible on the `componentVersion`"""
    )
    @FilterProperty
    @OrderProperty
    var invisibleSelfDefined: Boolean
) : ExtensibleNode() {

    companion object {
        const val VISIBLE_DERIVED_BY = "VISIBLE_DERIVED_BY"
        const val INVISIBLE_DERIVED_BY = "INVISIBLE_DERIVED_BY"
        const val INTERFACE_SPECIFICATION_VERSION = "INTERFACE_SPECIFICATION_VERSION"
        const val COMPONENT_VERSION = "COMPONENT_VERSION"
    }

    @NodeRelationship(INTERFACE_SPECIFICATION_VERSION, Direction.OUTGOING)
    @GraphQLDescription("The InterfaceSpecificationVersion present on the ComponentVersion")
    @FilterProperty
    @delegate:Transient
    val interfaceSpecificationVersion by NodeProperty<InterfaceSpecificationVersion>()

    @NodeRelationship(COMPONENT_VERSION, Direction.OUTGOING)
    @GraphQLDescription("The ComponentVersion using the InterfaceSpecificationVersion")
    @FilterProperty
    @delegate:Transient
    val componentVersion by NodeProperty<ComponentVersion>()

    @NodeRelationship(VISIBLE_DERIVED_BY, Direction.OUTGOING)
    @GraphQLDescription(
        """Relations because of which `interfaceSpecificationVersion` is visible derived on `componentVersion`"""
    )
    @FilterProperty
    @delegate:Transient
    val visibleDerivedBy by NodeSetProperty<Relation>()

    @NodeRelationship(INVISIBLE_DERIVED_BY, Direction.OUTGOING)
    @GraphQLDescription(
        """Relations because of which `interfaceSpecificationVersion` is invisible derived on `componentVersion`"""
    )
    @FilterProperty
    @delegate:Transient
    val invisibleDerivedBy by NodeSetProperty<Relation>()

    @NodeRelationship(Interface.DEFINITION, Direction.INCOMING)
    @GraphQLDescription("If visible, the created Interface")
    @FilterProperty
    @delegate:Transient
    val visibleInterface by NodeProperty<Interface?>()
}