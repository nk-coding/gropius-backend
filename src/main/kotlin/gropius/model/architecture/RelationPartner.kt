package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription("Entity which can be used as start / end of Relations. Can be affected by Issues.")
abstract class RelationPartner(name: String, description: String) : AffectedByIssue(name, description) {
    companion object {
        const val INGOING_RELATION = "INGOING_RELATION"
        const val OUTGOING_RELATION = "OUTGOING_RELATION"
    }

    @NodeRelationship(INGOING_RELATION, Direction.OUTGOING)
    @GraphQLDescription("Relations which use this as the end of the Relation.")
    @FilterProperty
    @delegate:Transient
    val ingoingRelations by NodeSetProperty<Relation>()

    @NodeRelationship(OUTGOING_RELATION, Direction.OUTGOING)
    @GraphQLDescription("Relations which use this as the start of the Relation.")
    @FilterProperty
    @delegate:Transient
    val outgoingRelations by NodeSetProperty<Relation>()
}