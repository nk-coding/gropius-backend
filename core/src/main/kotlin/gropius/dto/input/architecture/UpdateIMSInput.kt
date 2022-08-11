package gropius.dto.input.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.execution.OptionalInput
import gropius.dto.input.common.JSONFieldInput
import gropius.dto.input.common.UpdateNamedNodeInput
import gropius.dto.input.common.ensureNoDuplicates
import gropius.dto.input.ifPresent
import gropius.dto.input.template.UpdateTemplatedNodeInput

@GraphQLDescription("Input for the updateIMS mutation")
class UpdateIMSInput(
    @GraphQLDescription("Values for templatedFields to update")
    override val templatedFields: OptionalInput<List<JSONFieldInput>>
) : UpdateNamedNodeInput(), UpdateTemplatedNodeInput {

    override fun validate() {
        super.validate()
        templatedFields.ifPresent {
            it.ensureNoDuplicates()
        }
    }
}