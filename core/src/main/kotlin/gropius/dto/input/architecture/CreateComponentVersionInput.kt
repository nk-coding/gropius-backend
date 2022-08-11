package gropius.dto.input.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID

@GraphQLDescription("Input for the createComponentVersion mutation")
class CreateComponentVersionInput(
    @GraphQLDescription("The id of the Component the created ComponentVersion is part of")
    val component: ID
) : ComponentVersionInput()