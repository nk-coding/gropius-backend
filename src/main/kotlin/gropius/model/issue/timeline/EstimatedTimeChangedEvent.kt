package gropius.model.issue.timeline

import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import java.time.Duration
import java.time.OffsetDateTime

@DomainNode
class EstimatedTimeChangedEvent(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @FilterProperty
    val oldEstimatedTime: Duration?,
    @FilterProperty
    val newEstimatedTime: Duration?
) : TimelineItem(createdAt, lastModifiedAt) {}