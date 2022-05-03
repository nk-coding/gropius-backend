package gropius.model.template

import gropius.model.architecture.Relation
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient


@DomainNode("relationTemplates")
class RelationTemplate(
    name: String, description: String, isDeprecated: Boolean
) : Template<Relation, RelationTemplate>(name, description, isDeprecated) {

    @NodeRelationship(RelationCondition.PART_OF, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val relationConditions by NodeSetProperty<RelationCondition>()

}