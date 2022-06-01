package gropius.model.user.permission

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.graphql.TypeGraphQLType
import gropius.model.architecture.Component
import gropius.model.architecture.Interface
import gropius.model.architecture.Relation
import io.github.graphglue.model.DomainNode

/**
 * The name of the IMSPermissionEntry GraphQL enum
 */
const val COMPONENT_PERMISSION_ENTRY_NAME = "ComponentPermissionEntry"

@DomainNode
@GraphQLDescription("NodePermission to grant specific permissions to a set of Components.")
class ComponentPermission(
    entries: MutableList<String>, allUsers: Boolean
) : TrackablePermission<Component>(entries, allUsers) {

    companion object {
        /**
         * Permission to check if a user can create [Relation]s with a version of the [Component]
         * or an [Interface] of the [Component] as end
         */
        const val RELATE_TO_COMPONENT = "RELATE_TO_COMPONENT"

        /**
         * Permission to check if a user can create [Relation]s with a version of the [Component]
         * or an [Interface] of the [Component] as start
         */
        const val RELATE_FROM_COMPONENT = "RELATE_FROM_COMPONENT"
    }

    @GraphQLDescription(ENTRIES_DESCRIPTION)
    override val entries: MutableList<@TypeGraphQLType(COMPONENT_PERMISSION_ENTRY_NAME) String>
        get() = super.entries
}