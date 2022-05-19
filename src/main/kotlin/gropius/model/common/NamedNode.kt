package gropius.model.common

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import io.github.graphglue.model.DomainNode

@DomainNode
@GraphQLDescription("ExtensibleNode with a name and description")
abstract class NamedNode(
    @property:GraphQLDescription("The name of this entity.")
    override var name: String,
    @property:GraphQLDescription("The description of this entity.")
    override var description: String
) : ExtensibleNode(), Named