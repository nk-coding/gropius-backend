package gropius.model.issue.timeline

import io.github.graphglue.model.*
import java.time.OffsetDateTime
import org.springframework.data.annotation.Transient

@DomainNode
class RelatedByIssueEvent(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val RELATION = "RELATION"
    }

    @NodeRelationship(RELATION, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val relation by NodeProperty<IssueRelation>()

}