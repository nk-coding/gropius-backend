package gropius.model.architecture

import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

@DomainNode
class ComponentVersion(
    name: String, description: String, override var version: String
) : RelationPartner(name, description), Versioned {

    companion object {
        const val VISIBLE_SELF_DEFINED = "VISIBLE_SELF_DEFINED"
        const val INVISIBLE_SELF_DEFINED = "INVISIBLE_SELF_DEFINED"
        const val VISIBLE_DERIVED = "VISIBLE_DERIVED"
        const val INVISIBLE_DERIVED = "INVISIBLE_DERIVED"
    }

    @NodeRelationship(Component.VERSION, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val component by NodeProperty<Component>()

    @NodeRelationship(Interface.COMPONENT, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val interfaces by NodeSetProperty<Interface>()

    @NodeRelationship(Project.COMPONENT, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val includingProjects by NodeSetProperty<Project>()

    @NodeRelationship(VISIBLE_SELF_DEFINED, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val visibleSelfDefinedInterfaceSpecificationVersions by NodeSetProperty<InterfaceSpecificationVersion>()

    @NodeRelationship(INVISIBLE_SELF_DEFINED, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val invisibleSelfDefinedInterfaceSpecificationVersions by NodeSetProperty<InterfaceSpecificationVersion>()

    @NodeRelationship(VISIBLE_DERIVED, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val visibleDerivedInterfaceSpecificationVersions by NodeSetProperty<InterfaceSpecificationVersion>()

    @NodeRelationship(INVISIBLE_DERIVED, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val invisibleDerivedInterfaceSpecificationVersions by NodeSetProperty<InterfaceSpecificationVersion>()
}