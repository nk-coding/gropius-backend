package gropius.model.issue.timeline

import gropius.model.issue.Label
import io.github.graphglue.model.Direction
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

class UnlabeledEvent(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) :
    TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val REMOVED_LABEL = "REMOVED_LABEL"
    }

    @NodeRelationship(REMOVED_LABEL, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var removedLabel by NodeProperty<Label>()

}