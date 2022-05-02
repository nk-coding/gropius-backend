package gropius.model.architecture

import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
class InterfacePart(name: String, description: String) : ServiceEffectSpecificationLocation(name, description) {

    companion object {
        const val DEFINED_ON = "DEFINED_ON"
    }

    @NodeRelationship(Relation.START_PART, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val includingOutgoingRelations by NodeSetProperty<Relation>()

    @NodeRelationship(Relation.END_PART, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val includingIngoingRelations by NodeSetProperty<Relation>()

    @NodeRelationship(InterfaceSpecificationVersion.ACTIVE_PART, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val activeOn by NodeSetProperty<InterfaceSpecificationVersion>()

    @NodeRelationship(DEFINED_ON, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val definedOn by NodeProperty<InterfaceSpecification>()

}