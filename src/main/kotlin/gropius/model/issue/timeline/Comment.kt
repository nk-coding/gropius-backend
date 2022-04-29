package gropius.model.issue.timeline

import gropius.model.issue.Artefact
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

@DomainNode
abstract class Comment(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @FilterProperty var body: String,
    @FilterProperty @OrderProperty var lastEditedAt: OffsetDateTime
) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val REFERENCED_ARTEFACT = "REFERENCED_ARTEFACT"
    }

    @NodeRelationship(IssueComment.ANSWERS, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val answeredBy by NodeSetProperty<IssueComment>()

    @NodeRelationship(REFERENCED_ARTEFACT, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val referencedArtefacts by NodeSetProperty<Artefact>()

}