package gropius.model.issue.timeline

import gropius.model.issue.Artefact
import io.github.graphglue.model.Direction
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

class RemovedArtefactEvent(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) :
    TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val REMOVED_ARTEFACT = "REMOVED_ARTEFACT"
    }

    @NodeRelationship(REMOVED_ARTEFACT, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var removedArtefact by NodeProperty<Artefact>()

}