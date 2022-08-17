package gropius.dto.input.user

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.execution.OptionalInput

@GraphQLDescription("Input for the updateGropiusUserMutation")
class UpdateGropiusUserInput(
    @GraphQLDescription("The new value for isAdmin of the GropiusUser to update")
    val isAdmin: OptionalInput<Boolean>
) : UpdateUserInput()