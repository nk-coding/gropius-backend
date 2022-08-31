package gropius.dto.input.user

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.generator.scalars.ID

@GraphQLDescription("Input for the updateIMSUser mutation")
class UpdateIMSUserInput(
    @GraphQLDescription("The new username of the updated IMSUser")
    val username: OptionalInput<String?>,
    @GraphQLDescription(
        """The id of the GropiusUser the updated IMSUser is associated with, replaces existing association 
        or removes it if null is provided.
        """
    )
    val gropiusUser: OptionalInput<ID?>
) : UpdateUserInput()