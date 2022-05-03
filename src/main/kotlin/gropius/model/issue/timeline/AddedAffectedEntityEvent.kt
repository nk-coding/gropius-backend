package gropius.model.issue.timeline

import gropius.model.architecture.AffectedByIssue
import io.github.graphglue.model.Direction
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

class AddedAffectedEntityEvent(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) :
    TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val ADDED_AFFECTED = "ADDED_AFFECTED"
    }

    @NodeRelationship(ADDED_AFFECTED, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var addedAffectedEntity by NodeProperty<AffectedByIssue>()

}