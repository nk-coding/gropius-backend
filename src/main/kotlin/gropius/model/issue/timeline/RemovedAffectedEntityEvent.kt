package gropius.model.issue.timeline

import gropius.model.architecture.AffectedByIssue
import io.github.graphglue.model.Direction
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

class RemovedAffectedEntityEvent(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) :
    TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val REMOVED_AFFECTED = "REMOVED_AFFECTED"
    }

    @NodeRelationship(REMOVED_AFFECTED, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var removedAffectedEntity by NodeProperty<AffectedByIssue>()

}