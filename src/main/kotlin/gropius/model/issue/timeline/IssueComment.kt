package gropius.model.issue.timeline

import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

@DomainNode
abstract class IssueComment(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    body: String,
    lastEditedAt: OffsetDateTime
) : Comment(createdAt, lastModifiedAt, body, lastEditedAt) {

    companion object {
        const val ANSWERS = "ANSWERS"
    }

    @NodeRelationship(ANSWERS, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val answers by NodeProperty<Comment>()

}