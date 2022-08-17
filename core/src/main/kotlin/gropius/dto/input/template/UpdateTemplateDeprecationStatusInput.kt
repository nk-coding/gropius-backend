package gropius.dto.input.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.dto.input.common.UpdateNodeInput

@GraphQLDescription("Input for the updateTemplateDeprecationStatus mutation")
class UpdateTemplateDeprecationStatusInput(
    @GraphQLDescription("The new deprecation status of the template")
    val isDeprecated: Boolean
) : UpdateNodeInput()