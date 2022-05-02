package gropius.model.issue.timeline

import io.github.graphglue.model.Direction
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

class UnassignedEvent(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) :
    TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val REMOVED_ASSIGNMENT = "REMOVED_ASSIGNMENT"
    }

    @NodeRelationship(REMOVED_ASSIGNMENT, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val removedAssignment by NodeProperty<Assignment>()

}