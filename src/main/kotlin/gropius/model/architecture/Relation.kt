package gropius.model.architecture

import gropius.model.common.ExtensibleNode
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
class Relation : ExtensibleNode() {

    companion object {
        const val START_PART = "START_PART"
        const val END_PART = "END_PART"
    }

    @NodeRelationship(RelationPartner.INGOING_RELATION, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    var end by NodeProperty<RelationPartner>()

    @NodeRelationship(RelationPartner.OUTGOING_RELATION, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    var start by NodeProperty<RelationPartner>()

    @NodeRelationship(START_PART, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val startParts by NodeSetProperty<InterfacePart>()

    @NodeRelationship(END_PART, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val endParts by NodeSetProperty<InterfacePart>()
}