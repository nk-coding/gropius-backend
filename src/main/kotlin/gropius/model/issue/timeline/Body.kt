package gropius.model.issue.timeline

import io.github.graphglue.model.DomainNode
import java.time.OffsetDateTime

@DomainNode
class Body(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    body: String,
    lastEditedAt: OffsetDateTime
) : Comment(createdAt, lastModifiedAt, body, lastEditedAt) {
}