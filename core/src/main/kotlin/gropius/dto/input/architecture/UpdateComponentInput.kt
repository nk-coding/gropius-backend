package gropius.dto.input.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.generator.scalars.ID
import gropius.dto.input.common.JSONFieldInput
import gropius.dto.input.template.UpdateTemplatedNodeInput

@GraphQLDescription("Input for the update")
class UpdateComponentInput(
    @GraphQLDescription("Values for templatedFields to update")
    override val templatedFields: OptionalInput<List<JSONFieldInput>>,
    @GraphQLDescription(
        """If provided, the new template fo the Component
        Use `templatedFields` to update fields so that they conform with the new specifications.
        Use `componentVersionTemplatedFields` to update the `templatedFields` of ALL ComponentVersions
        No longer needed fields are automatically removed.
        """
    )
    val template: OptionalInput<ID>,
    @GraphQLDescription(
        """Values for templatedFields of ComponentVersions to update.
        Only evaluated if `template` is provided!
        Affect all ComponentVersions of the updated Component
        """
    )
    val componentVersionTemplatedFields: OptionalInput<List<JSONFieldInput>>
) : UpdateTrackableInput(), UpdateTemplatedNodeInput