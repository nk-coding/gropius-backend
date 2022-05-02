package gropius.model.issue.timeline

import gropius.model.architecture.Trackable
import io.github.graphglue.model.Direction
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import java.time.OffsetDateTime
import org.springframework.data.annotation.Transient

class PinnedEvent(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val PINNED_ON = "PINNED_ON"
    }

    @NodeRelationship(PINNED_ON, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val pinnedOn by NodeProperty<Trackable>()

}