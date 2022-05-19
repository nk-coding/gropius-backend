package gropius.model.user

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.common.ExtensibleNode
import gropius.model.common.AuditedNode
import gropius.model.issue.Issue
import gropius.model.issue.timeline.Assignment
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription(
    """A user known to the Gropius System.
    This might be a user that registered directly, or a user the systems know via a sync adapter.
    A user can create AuditedNodes, participate in Issues and be assigned to Issues.
    """
)
@AdditionalFilter("usernameFilter")
abstract class User(
    @property:GraphQLDescription("The name which should be displayed for the user.")
    @FilterProperty
    @OrderProperty
    var displayName: String,
    @property:GraphQLDescription("The email address of the user.")
    @FilterProperty
    @OrderProperty
    var email: String?
) : ExtensibleNode() {

    @GraphQLDescription(
        """The identifier of the user.
        This is only unique for GropiusUsers, for IMSUsers, no constrains are guaranteed.
        """
    )
    abstract fun username(): String?

    @NodeRelationship(AuditedNode.CREATED_BY, Direction.INCOMING)
    @GraphQLDescription("AuditedNodes the user created.")
    @FilterProperty
    @delegate:Transient
    val createdNodes by NodeSetProperty<AuditedNode>()

    @NodeRelationship(Issue.PARTICIPANT, Direction.INCOMING)
    @GraphQLDescription("Issues the user participated in.")
    @FilterProperty
    @delegate:Transient
    val participatedIssues by NodeSetProperty<Issue>()

    @NodeRelationship(Assignment.USER, Direction.INCOMING)
    @GraphQLDescription("Assignments the user is part of, this includes assignments which aren't active.")
    @FilterProperty
    @delegate:Transient
    val assignments by NodeSetProperty<Assignment>()
}