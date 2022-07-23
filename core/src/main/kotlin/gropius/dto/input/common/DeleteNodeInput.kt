package gropius.dto.input.common

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID

@GraphQLDescription("Input for all delete mutations. Deletes the node with the specified id")
class DeleteNodeInput(
    @GraphQLDescription("The id of the Node to delete")
    val id: ID
)