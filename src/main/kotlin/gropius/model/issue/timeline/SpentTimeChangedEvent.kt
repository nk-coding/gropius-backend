package gropius.model.issue.timeline

import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import java.time.Duration
import java.time.OffsetDateTime

@DomainNode
class SpentTimeChangedEvent(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @FilterProperty
    val oldSpentTime: Duration?,
    @FilterProperty
    val newSpentTime: Duration?
) : TimelineItem(createdAt, lastModifiedAt) {}