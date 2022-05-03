package gropius.model.issue.timeline

import gropius.model.architecture.Trackable
import io.github.graphglue.model.Direction
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

class PinnedEvent(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val PINNED_ON = "PINNED_ON"
    }

    @NodeRelationship(PINNED_ON, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var pinnedOn by NodeProperty<Trackable>()

}