package gropius.model.issue.timeline

import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import java.time.OffsetDateTime

@DomainNode
class StartDateChangedEvent(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @FilterProperty
    val oldStartDate: OffsetDateTime?,
    @FilterProperty
    val newStartDate: OffsetDateTime?
) : TimelineItem(createdAt, lastModifiedAt) {}