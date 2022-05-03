package gropius.model.issue.timeline

import gropius.model.template.IssuePriority
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

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
    var oldPriority by NodeProperty<IssuePriority>()

    @NodeRelationship(NEW_PRIORITY, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var newPriority by NodeProperty<IssuePriority>()
}