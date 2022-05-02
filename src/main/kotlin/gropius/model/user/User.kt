package gropius.model.user

import gropius.model.common.ExtensibleNode
import gropius.model.common.SyncNode
import gropius.model.issue.Issue
import gropius.model.issue.timeline.Assignment
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

@DomainNode
@AdditionalFilter("usernameFilter")
abstract class User(
    @FilterProperty @OrderProperty var displayName: String,
    @FilterProperty @OrderProperty var email: String?
) : ExtensibleNode() {
    abstract fun username(): String?

    @NodeRelationship(SyncNode.CREATED_BY, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val createdNodes by NodeSetProperty<SyncNode>()

    @NodeRelationship(Issue.PARTICIPANT, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val participatedIssues by NodeSetProperty<Issue>()

    @NodeRelationship(Assignment.USER, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val assignments by NodeSetProperty<Assignment>()
}