package gropius.dto.input.user.permission

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.graphql.TypeGraphQLType
import gropius.model.user.permission.COMPONENT_PERMISSION_ENTRY_NAME

@GraphQLDescription("Input for the createComponentPermission mutation")
class CreateComponentPermissionInput(
    @GraphQLDescription("The initial entries of the created ComponentPermission")
    override val entries: List<@TypeGraphQLType(COMPONENT_PERMISSION_ENTRY_NAME) String>
) : CreateNodePermissionInput()