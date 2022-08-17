package gropius.dto.input.user

import com.expediagroup.graphql.generator.annotations.GraphQLDescription

@GraphQLDescription("Input for the createGropiusUser mutation")
class CreateGropiusUserInput(
    @GraphQLDescription("The username of the created GropiusUser, must be unique, must match /^[a-zA-Z0-9_-]+$/")
    val username: String,
    @GraphQLDescription("If true, the created GropiusUser is a global admin")
    val isAdmin: Boolean
) : CreateUserInput() {

    override fun validate() {
        super.validate()
        if (!"^[a-zA-Z0-9_-]+$".toRegex().matches(username)) {
            throw IllegalStateException("Invalid username")
        }
    }
}