package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.common.NamedNode
import gropius.model.issue.Issue
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription("Priority of an Issue like HIGH or LOW. Part of an IssueTemplate.")
class IssuePriority(
    name: String,
    description: String,
    @property:GraphQLDescription("The value of the IssuePriority, used to compare/order different IssuePriorities.")
    @FilterProperty
    @OrderProperty
    val value: Double
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