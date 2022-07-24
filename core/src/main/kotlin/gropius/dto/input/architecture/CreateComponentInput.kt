package gropius.dto.input.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import gropius.dto.input.common.JSONFieldInput
import gropius.dto.input.template.CreateTemplatedNodeInput

@GraphQLDescription("Input for the createComponent mutation")
class CreateComponentInput(
    @GraphQLDescription("Initial values for all templatedFields")
    override val templatedFields: List<JSONFieldInput>,
    @GraphQLDescription("The template of the created Component")
    val template: ID
) : CreateTrackableInput(), CreateTemplatedNodeInput