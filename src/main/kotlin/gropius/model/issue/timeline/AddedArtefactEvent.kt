package gropius.model.issue.timeline

import gropius.model.issue.Artefact
import io.github.graphglue.model.Direction
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

class AddedArtefactEvent(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) :
    TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val ADDED_ARTEFACT = "ADDED_ARTEFACT"
    }

    @NodeRelationship(ADDED_ARTEFACT, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var addedArtefact by NodeProperty<Artefact>()

}