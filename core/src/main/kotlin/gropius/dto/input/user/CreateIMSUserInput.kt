package gropius.dto.input.user

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID

@GraphQLDescription("Input for the createIMSUser mutation")
class CreateIMSUserInput(
    @GraphQLDescription("The username of the created IMSUser, must be unique")
    val username: String?,
    @GraphQLDescription("The id of the IMS the created IMSUser is part of")
    val ims: ID,
    @GraphQLDescription("If present, the id of the GropiusUser the created IMSUser is associated with")
    val gropiusUser: ID?
) : CreateUserInput()