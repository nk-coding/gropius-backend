package gropius.dto.input.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import gropius.dto.input.common.Input

@GraphQLDescription("Input for the removeComponentVersionFromProject mutation")
class RemoveComponentVersionFromProjectInput(
    @GraphQLDescription("The id of the ComponentVersion to remove")
    val componentVersion: ID,
    @GraphQLDescription("The id of the Project from which the ComponentVersion is removed")
    val project: ID
) : Input()