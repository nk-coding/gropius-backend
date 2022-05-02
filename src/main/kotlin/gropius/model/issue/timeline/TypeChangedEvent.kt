package gropius.model.issue.timeline

import gropius.model.template.IssueType
import io.github.graphglue.model.*
import java.time.OffsetDateTime
import org.springframework.data.annotation.Transient

@DomainNode
class TypeChangedEvent(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val OLD_TYPE = "OLD_TYPE"
        const val NEW_TYPE = "NEW_TYPE"
    }

    @NodeRelationship(OLD_TYPE, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val oldType by NodeProperty<IssueType>()

    @NodeRelationship(NEW_TYPE, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val newType by NodeProperty<IssueType>()
}