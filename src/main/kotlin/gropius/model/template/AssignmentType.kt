package gropius.model.template

import gropius.model.common.NamedNode
import gropius.model.issue.timeline.Assignment
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
class AssignmentType(name: String, description: String) : NamedNode(name, description) {

    companion object {
        const val PART_OF = "PART_OF"
    }

    @NodeRelationship(Assignment.TYPE, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val assignmentsWithType by NodeSetProperty<Assignment>()

    @NodeRelationship(PART_OF, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val partOf by NodeSetProperty<IssueTemplate>()

}