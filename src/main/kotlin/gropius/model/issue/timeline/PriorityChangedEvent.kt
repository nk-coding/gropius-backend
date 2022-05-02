package gropius.model.issue.timeline

import gropius.model.template.IssuePriority
import io.github.graphglue.model.*
import java.time.OffsetDateTime
import org.springframework.data.annotation.Transient

@DomainNode
class PriorityChangedEvent(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val OLD_PRIORITY = "OLD_PRIORITY"
        const val NEW_PRIORITY = "NEW_PRIORITY"
    }

    @NodeRelationship(OLD_PRIORITY, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val oldPriority by NodeProperty<IssuePriority>()

    @NodeRelationship(NEW_PRIORITY, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val newPriority by NodeProperty<IssuePriority>()
}