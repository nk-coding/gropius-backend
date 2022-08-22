package gropius.dto.input.user.permission

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.graphql.TypeGraphQLType
import gropius.model.user.permission.IMS_PERMISSION_ENTRY_NAME

@GraphQLDescription("Input for the createIMSPermission mutation")
class CreateIMSPermissionInput(
    @GraphQLDescription("The initial entries of the created IMSPermission")
    override val entries: List<@TypeGraphQLType(IMS_PERMISSION_ENTRY_NAME) String>
) : CreateNodePermissionInput()