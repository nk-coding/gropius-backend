package gropius.model.issue.timeline

import gropius.model.issue.Issue
import gropius.model.template.IssueRelationType
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

@DomainNode
class IssueRelation(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val TYPE = "TYPE"
        const val RELATED_ISSUE = "RELATED_ISSUE"
    }

    @NodeRelationship(TYPE, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val type by NodeProperty<IssueRelationType?>()

    @NodeRelationship(RELATED_ISSUE, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val relatedIssue by NodeProperty<Issue>()

}