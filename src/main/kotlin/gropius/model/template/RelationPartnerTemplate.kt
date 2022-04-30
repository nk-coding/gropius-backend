package gropius.model.template

import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

@DomainNode
abstract class RelationPartnerTemplate<T : Node, S : RelationPartnerTemplate<T, S>>(
    name: String, description: String, isDeprecated: Boolean
) : Template<T, S>(name, description, isDeprecated) {

    @NodeRelationship(RelationCondition.FROM, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val possibleStartOfRelations by NodeSetProperty<RelationCondition>()

    @NodeRelationship(RelationCondition.TO, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val possibleEndOfRelations by NodeSetProperty<RelationCondition>()

}