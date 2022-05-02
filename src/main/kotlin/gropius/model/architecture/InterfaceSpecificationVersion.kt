package gropius.model.architecture

import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
class InterfaceSpecificationVersion(
    name: String, description: String, override var version: String
) : ServiceEffectSpecificationLocation(name, description), Versioned {

    companion object {
        const val ACTIVE_PART = "ACTIVE_PART"
    }

    @NodeRelationship(ACTIVE_PART, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val activeParts by NodeSetProperty<InterfacePart>()

    @NodeRelationship(InterfaceSpecification.VERSION, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val interfaceSpecification by NodeProperty<InterfaceSpecification>()

    @NodeRelationship(Interface.SPECIFICATION, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val interfaces by NodeSetProperty<Interface>()

    @NodeRelationship(ComponentVersion.VISIBLE_SELF_DEFINED, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val visibleSelfDefinedOn by NodeSetProperty<ComponentVersion>()

    @NodeRelationship(ComponentVersion.INVISIBLE_SELF_DEFINED, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val invisibleSelfDefinedOn by NodeSetProperty<ComponentVersion>()

    @NodeRelationship(ComponentVersion.VISIBLE_DERIVED, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val visibleDerivedOn by NodeSetProperty<ComponentVersion>()

    @NodeRelationship(ComponentVersion.INVISIBLE_DERIVED, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val invisibleDerivedOn by NodeSetProperty<ComponentVersion>()

}