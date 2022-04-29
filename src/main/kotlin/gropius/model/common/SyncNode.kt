package gropius.model.common

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.OrderProperty
import java.time.OffsetDateTime

@DomainNode
abstract class SyncNode(
    @FilterProperty @OrderProperty @GraphQLIgnore val createdAt: OffsetDateTime,
    @FilterProperty @OrderProperty @GraphQLIgnore var lastModifiedAt: OffsetDateTime
) : ExtensibleNode() {
    internal var isDeleted: Boolean = false
}