package gropius.model.issue.timeline

import gropius.model.issue.Artefact
import gropius.model.user.User
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

@DomainNode
abstract class Comment(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @FilterProperty open var body: String,
    @FilterProperty @OrderProperty var lastEditedAt: OffsetDateTime
) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val REFERENCED_ARTEFACT = "REFERENCED_ARTEFACT"
        const val LAST_EDITED_BY = "LAST_EDITED_BY"
    }

    @NodeRelationship(IssueComment.ANSWERS, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val answeredBy by NodeSetProperty<IssueComment>()

    @NodeRelationship(REFERENCED_ARTEFACT, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val referencedArtefacts by NodeSetProperty<Artefact>()

    @NodeRelationship(LAST_EDITED_BY, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var lastEditedBy by NodeProperty<User>()

}