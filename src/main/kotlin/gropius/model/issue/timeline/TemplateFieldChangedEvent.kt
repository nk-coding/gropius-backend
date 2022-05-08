package gropius.model.issue.timeline

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLType
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import java.time.OffsetDateTime

@DomainNode
@GraphQLDescription("Event representing that the value of a templated field changed.")
class TemplateFieldChangedEvent(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @GraphQLDescription("The name of the templated field.")
    @FilterProperty
    val fieldName: String,
    @GraphQLDescription("The old value of the templated field.")
    @GraphQLType("JSON")
    @FilterProperty
    val oldValue: Any?,
    @GraphQLDescription("The new value of the templated field.")
    @GraphQLType("JSON")
    @FilterProperty
    val newValue: Any?
) : TimelineItem(createdAt, lastModifiedAt) {}