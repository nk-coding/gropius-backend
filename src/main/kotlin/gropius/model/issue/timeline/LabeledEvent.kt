package gropius.model.issue.timeline

import gropius.model.issue.Label
import io.github.graphglue.model.Direction
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

class LabeledEvent(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) :
    TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val ADDED_LABEL = "ADDED_LABEL"
    }

    @NodeRelationship(ADDED_LABEL, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val addedLabel by NodeProperty<Label>()

}