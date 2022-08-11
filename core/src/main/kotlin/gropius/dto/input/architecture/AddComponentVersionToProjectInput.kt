package gropius.dto.input.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import gropius.dto.input.common.Input

@GraphQLDescription("Input for the addComponentVersionToProject mutation")
class AddComponentVersionToProjectInput(
    @GraphQLDescription("The id of the ComponentVersion to add")
    val componentVersion: ID,
    @GraphQLDescription("The id of the Project where to add the ComponentVersion")
    val project: ID
) : Input()