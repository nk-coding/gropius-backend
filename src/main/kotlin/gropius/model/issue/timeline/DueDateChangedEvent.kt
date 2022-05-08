package gropius.model.issue.timeline

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import java.time.OffsetDateTime

@DomainNode
@GraphQLDescription("Event representing that the due date of an Issue changed.")
class DueDateChangedEvent(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @GraphQLDescription("The old due date.")
    @FilterProperty
    val oldDueDate: OffsetDateTime?,
    @GraphQLDescription("The new due date.")
    @FilterProperty
    val newDueDate: OffsetDateTime?
) : TimelineItem(createdAt, lastModifiedAt) {}