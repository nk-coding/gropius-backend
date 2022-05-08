package gropius.model.issue.timeline

import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import java.time.OffsetDateTime

@DomainNode
class DueDateChangedEvent(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @FilterProperty
    val oldDueDate: OffsetDateTime?,
    @FilterProperty
    val newDueDate: OffsetDateTime?
) : TimelineItem(createdAt, lastModifiedAt) {}