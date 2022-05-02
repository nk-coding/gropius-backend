package gropius.model.issue.timeline

import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

@DomainNode
class RemovedIncomingRelationEvent(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val REMOVES = "REMOVES"
    }

    @NodeRelationship(REMOVES, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val removedRelation by NodeProperty<IssueRelation>()

}