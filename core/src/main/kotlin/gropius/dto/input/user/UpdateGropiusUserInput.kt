package gropius.dto.input.user

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.execution.OptionalInput
import gropius.dto.input.common.UpdateExtensibleNodeInput

@GraphQLDescription("Input for the updateGropiusUserMutation")
class UpdateGropiusUserInput(
    @GraphQLDescription("The new displayName of the GropiusUser to update")
    val displayName: OptionalInput<String>,
    @GraphQLDescription("The new email of the GropiusUser to update")
    val email: OptionalInput<String?>,
    @GraphQLDescription("The new value for isAdmin of the GropiusUser to update")
    val isAdmin: OptionalInput<Boolean>
) : UpdateExtensibleNodeInput()