package gropius.dto.input.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.generator.scalars.ID
import gropius.dto.input.common.JSONFieldInput
import gropius.dto.input.common.UpdateNamedNodeInput
import gropius.dto.input.common.ensureNoDuplicates
import gropius.dto.input.ensureDistinct
import gropius.dto.input.ifPresent
import gropius.dto.input.template.UpdateTemplatedNodeInput

@GraphQLDescription("Input for the updateIMS mutation")
class UpdateIMSInput(
    @GraphQLDescription("Values for templatedFields to update")
    override val templatedFields: OptionalInput<List<JSONFieldInput>>,
    @GraphQLDescription("Ids of permissions to add, must be distinct with removedPermissions.")
    val addedPermissions: OptionalInput<List<ID>>,
    @GraphQLDescription(
        """Ids of permissions to remove, must be distinct with addedPermissions.
        There must always be at least one permissions granting ADMIN to some GropiusUser left.
        """
    )
    val removedPermissions: OptionalInput<List<ID>>
) : UpdateNamedNodeInput(), UpdateTemplatedNodeInput {

    override fun validate() {
        super.validate()
        templatedFields.ifPresent {
            it.ensureNoDuplicates()
        }
        ::addedPermissions ensureDistinct ::removedPermissions
    }
}