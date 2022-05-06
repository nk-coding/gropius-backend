package gropius.model.common

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import io.github.graphglue.model.DomainNode

@DomainNode
@GraphQLDescription("ExtensibleNode with a name and description")
abstract class NamedNode(
    @GraphQLDescription("The name of this entity.")
    override var name: String,
    @GraphQLDescription("The description of this entity.")
    override var description: String
) : ExtensibleNode(), Named