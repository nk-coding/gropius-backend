package gropius.model.issue.timeline

import io.github.graphglue.model.DomainNode
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

@DomainNode
abstract class Body(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    body: String,
    lastEditedAt: OffsetDateTime
) : Comment(createdAt, lastModifiedAt, body, lastEditedAt) {
}