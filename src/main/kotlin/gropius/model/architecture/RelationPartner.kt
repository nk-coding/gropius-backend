package gropius.model.architecture

import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

@DomainNode
abstract class RelationPartner(name: String, description: String) : AffectedByIssue(name, description) {
    companion object {
        const val INGOING_RELATION = "INGOING_RELATION"
        const val OUTGOING_RELATION = "OUTGOING_RELATION"
    }

    @NodeRelationship(INGOING_RELATION, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val ingoingRelations by NodeSetProperty<Relation>()

    @NodeRelationship(OUTGOING_RELATION, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val outgoingRelations by NodeSetProperty<Relation>()
}