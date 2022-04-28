package gropius.model.architecture

import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

@DomainNode
class InterfaceSpecification(name: String, description: String) : ServiceEffectSpecificationLocation(name, description) {

    companion object {
        const val VERSION = "VERSION"
        const val COMPONENT = "COMPONENT"
    }

    @NodeRelationship(VERSION, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val versions by NodeSetProperty<InterfaceSpecificationVersion>()

    @NodeRelationship(InterfacePart.DEFINED_ON, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val definedParts by NodeSetProperty<InterfacePart>()

    @NodeRelationship(COMPONENT, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val component by NodeProperty<Component>()
}