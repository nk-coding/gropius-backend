package gropius.model.template

import gropius.model.issue.Issue
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

@DomainNode
class IssueTemplate(
    name: String, description: String, isDeprecated: Boolean
) : Template<Issue, IssueTemplate>(name, description, isDeprecated) {

    @NodeRelationship(IssueType.PART_OF, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val issueTypes by NodeSetProperty<IssueType>()

    @NodeRelationship(AssignmentType.PART_OF, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val assignmentTypes by NodeSetProperty<AssignmentType>()

    @NodeRelationship(IssuePriority.PART_OF, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val issuePriorities by NodeSetProperty<IssuePriority>()

    @NodeRelationship(IssueRelationType.PART_OF, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val relationTypes by NodeSetProperty<IssueRelationType>()

}