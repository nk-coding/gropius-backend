package gropius.model.issue.timeline

import gropius.model.architecture.Trackable
import io.github.graphglue.model.Direction
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import java.time.OffsetDateTime
import org.springframework.data.annotation.Transient

class UnpinnedEvent(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) :
    TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val UNPINNED_ON = "UNPINNED_ON"
    }

    @NodeRelationship(UNPINNED_ON, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val unpinnedOn by NodeProperty<Trackable>()

}