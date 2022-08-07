package gropius.model.user.permission

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import io.github.graphglue.model.property.LazyLoadingDelegate
import io.github.graphglue.model.property.NodeSetPropertyDelegate

/**
 * Node with permissions
 */
@GraphQLIgnore
interface NodeWithPermissions<T : NodePermission<*>> {

    /**
     * Permissions associated with the NodeWithPermissions
     */
    @GraphQLIgnore
    val permissions: LazyLoadingDelegate<out T, out NodeSetPropertyDelegate<T>.NodeSetProperty>

}