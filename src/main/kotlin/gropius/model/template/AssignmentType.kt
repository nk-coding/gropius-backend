package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.common.NamedNode
import gropius.model.issue.timeline.Assignment
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription("Type for an Assignment, like REVIEWER. Part of an IssueTemplate.")
class AssignmentType(name: String, description: String) : NamedNode(name, description) {

    companion object {
        const val PART_OF = "PART_OF"
    }

    @NodeRelationship(Assignment.TYPE, Direction.INCOMING)
    @GraphQLDescription("Assignments which use this type.")
    @FilterProperty
    @delegate:Transient
    val assignmentsWithType by NodeSetProperty<Assignment>()

    @NodeRelationship(PART_OF, Direction.OUTGOING)
    @GraphQLDescription("IssueTemplates this is part of.")
    @FilterProperty
    @delegate:Transient
    val partOf by NodeSetProperty<IssueTemplate>()

}