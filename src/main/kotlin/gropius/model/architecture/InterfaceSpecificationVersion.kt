package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription(
    """A specific version of an InterfaceSpecification.
    Defines which InterfaceParts are active.
    Can be both visible (generates an Interface) and invisible (does not generate an Interface)
    on different Components.
    Can be derived by Relations, and affected by Issues.
    """
)
class InterfaceSpecificationVersion(
    name: String,
    description: String,
    @property:GraphQLDescription("The version of this InterfaceSpecificationVersion.")
    override var version: String
) : ServiceEffectSpecificationLocation(name, description), Versioned {

    companion object {
        const val ACTIVE_PART = "ACTIVE_PART"
    }

    @NodeRelationship(ACTIVE_PART, Direction.OUTGOING)
    @GraphQLDescription(
        """InterfaceParts which are active on this InterfaceSpecificationVersion
        Semantically, only the active parts on an InterfaceSpecificationVersion exist on the Interfaces
        defined by the InterfaceSpecificationVersion.
        """
    )
    @FilterProperty
    @delegate:Transient
    val activeParts by NodeSetProperty<InterfacePart>()

    @NodeRelationship(InterfaceSpecification.VERSION, Direction.INCOMING)
    @GraphQLDescription("The InterfaceSpecification this is part of.")
    @FilterProperty
    @delegate:Transient
    var interfaceSpecification by NodeProperty<InterfaceSpecification>()

    @NodeRelationship(Interface.SPECIFICATION, Direction.INCOMING)
    @GraphQLDescription("Interfaces this InterfaceSpecificationVersion defines.")
    @FilterProperty
    @delegate:Transient
    val interfaces by NodeSetProperty<Interface>()

    @NodeRelationship(ComponentVersion.VISIBLE_SELF_DEFINED, Direction.INCOMING)
    @GraphQLDescription("ComponentVersions where this is visible and self-defined")
    @FilterProperty
    @delegate:Transient
    val visibleSelfDefinedOn by NodeSetProperty<ComponentVersion>()

    @NodeRelationship(ComponentVersion.INVISIBLE_SELF_DEFINED, Direction.INCOMING)
    @GraphQLDescription("ComponentVersions where this is invisible and self-defined")
    @FilterProperty
    @delegate:Transient
    val invisibleSelfDefinedOn by NodeSetProperty<ComponentVersion>()

    @NodeRelationship(ComponentVersion.VISIBLE_DERIVED, Direction.INCOMING)
    @GraphQLDescription("ComponentVersions where this is visible and derived via a Relation")
    @FilterProperty
    @delegate:Transient
    val visibleDerivedOn by NodeSetProperty<ComponentVersion>()

    @NodeRelationship(ComponentVersion.INVISIBLE_DERIVED, Direction.INCOMING)
    @GraphQLDescription("ComponentVersions where this is invisible and derived via a Relation")
    @FilterProperty
    @delegate:Transient
    val invisibleDerivedOn by NodeSetProperty<ComponentVersion>()

}