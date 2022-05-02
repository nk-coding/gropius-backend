package gropius.model.template

import gropius.model.common.NamedNode
import gropius.model.issue.Issue
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient


@DomainNode
class IssuePriority(
    name: String, description: String, @FilterProperty @OrderProperty val value: Double
) : NamedNode(name, description) {

    companion object {
        const val PART_OF = "PART_OF"
    }

    @NodeRelationship(PART_OF, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val partOf by NodeSetProperty<IssueTemplate>()

    @NodeRelationship(Issue.PRIORITY, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val prioritizedIssues by NodeSetProperty<Issue>()
}