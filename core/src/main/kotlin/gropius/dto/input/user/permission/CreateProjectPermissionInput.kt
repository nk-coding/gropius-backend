package gropius.dto.input.user.permission

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.graphql.TypeGraphQLType
import gropius.model.user.permission.PROJECT_PERMISSION_ENTRY_NAME

@GraphQLDescription("Input for the createProjectPermission mutation")
class CreateProjectPermissionInput(
    @GraphQLDescription("The initial entries of the created ProjectPermission")
    override val entries: List<@TypeGraphQLType(PROJECT_PERMISSION_ENTRY_NAME) String>
) : CreateNodePermissionInput()