package gropius.model.common

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.OrderProperty
import java.time.OffsetDateTime

@DomainNode
@GraphQLDescription("AuditedNode with a name and description")
abstract class NamedAuditedNode(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @property:GraphQLDescription("The name of this entity.")
    @FilterProperty
    @OrderProperty
    override var name: String,
    @property:GraphQLDescription("The description of this entity.")
    @FilterProperty
    override var description: String
) : AuditedNode(createdAt, lastModifiedAt), Named