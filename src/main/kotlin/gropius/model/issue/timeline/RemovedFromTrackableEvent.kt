package gropius.model.issue.timeline

import gropius.model.architecture.Trackable
import io.github.graphglue.model.Direction
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

class RemovedFromTrackableEvent(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) :
    TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val REMOVED_FROM = "REMOVED_FROM"
    }

    @NodeRelationship(REMOVED_FROM, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val removedFromTrackable by NodeProperty<Trackable>()

}