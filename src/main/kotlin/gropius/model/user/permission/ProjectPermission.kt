package gropius.model.user.permission

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.graphql.TypeGraphQLType
import gropius.model.architecture.ComponentVersion
import gropius.model.architecture.Project
import io.github.graphglue.model.DomainNode

/**
 * The name of the IMSPermissionEntry GraphQL enum
 */
const val PROJECT_PERMISSION_ENTRY_NAME = "ProjectPermissionEntry"

@DomainNode
@GraphQLDescription("NodePermission to grant specific permissions to a set of Projects.")
class ProjectPermission(
    entries: MutableList<String>, allUsers: Boolean
) : TrackablePermission<Project>(entries, allUsers) {

    companion object {
        /**
         * Permission to check if a user is allowed to add / remove [ComponentVersion]s to / from the [Project]
         */
        const val MANAGE_COMPONENTS = "MANAGE_COMPONENTS"
    }

    @GraphQLDescription(ENTRIES_DESCRIPTION)
    override val entries: MutableList<@TypeGraphQLType(PROJECT_PERMISSION_ENTRY_NAME) String>
        get() = super.entries

}