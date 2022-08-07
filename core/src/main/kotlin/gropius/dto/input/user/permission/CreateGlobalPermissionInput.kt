package gropius.dto.input.user.permission

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.graphql.TypeGraphQLType
import gropius.model.user.permission.GLOBAL_PERMISSION_ENTRY_NAME

@GraphQLDescription("Input for the createGlobalPermission mutation")
class CreateGlobalPermissionInput(
    @GraphQLDescription("The initial entries of the created GlobalPermission")
    override val entries: List<@TypeGraphQLType(GLOBAL_PERMISSION_ENTRY_NAME) String>
) : CreateBasePermissionInput()