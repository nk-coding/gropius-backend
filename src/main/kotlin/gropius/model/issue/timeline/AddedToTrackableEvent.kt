package gropius.model.issue.timeline

import gropius.model.architecture.Trackable
import io.github.graphglue.model.Direction
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

class AddedToTrackableEvent(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) :
    TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val ADDED_TO = "ADDED_TO"
    }

    @NodeRelationship(ADDED_TO, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val addedToTrackable by NodeProperty<Trackable>()

}