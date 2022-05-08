package gropius.model.issue.timeline

import com.expediagroup.graphql.generator.annotations.GraphQLType
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import java.time.OffsetDateTime

@DomainNode
class TemplateFieldChangedEvent(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @FilterProperty
    val fieldName: String,
    @GraphQLType("JSON")
    @FilterProperty
    val oldValue: Any?,
    @GraphQLType("JSON")
    @FilterProperty
    val newValue: Any?
) : TimelineItem(createdAt, lastModifiedAt) {}