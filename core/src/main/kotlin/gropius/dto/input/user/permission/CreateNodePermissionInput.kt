package gropius.dto.input.user.permission

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import gropius.model.user.permission.NodePermission
import kotlin.properties.Delegates

/**
 * Fragment for create mutation inputs for classes extending [NodePermission]
 */
abstract class CreateNodePermissionInput : CreateBasePermissionInput() {

    @GraphQLDescription("The id of nodes the created permission affects.")
    var nodesWithPermission: List<ID> by Delegates.notNull()

}