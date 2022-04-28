package gropius.model.common

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.OrderProperty
import java.time.LocalDateTime

@DomainNode
abstract class SyncNode(
    @FilterProperty @OrderProperty @GraphQLIgnore val createdAt: LocalDateTime,
    @FilterProperty @OrderProperty @GraphQLIgnore var lastModifiedAt: LocalDateTime
) : ExtensibleNode()