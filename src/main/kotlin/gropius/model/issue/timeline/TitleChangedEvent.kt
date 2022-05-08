package gropius.model.issue.timeline

import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import java.time.OffsetDateTime

@DomainNode
class TitleChangedEvent(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @FilterProperty
    val oldTitle: String,
    @FilterProperty
    val newTitle: String
) : TimelineItem(createdAt, lastModifiedAt) {}