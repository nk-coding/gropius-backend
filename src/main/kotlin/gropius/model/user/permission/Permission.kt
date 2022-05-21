package gropius.model.user.permission

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.graphql.TypeGraphQLType
import gropius.model.architecture.Component
import gropius.model.architecture.Project
import gropius.model.template.Template
import gropius.model.user.GropiusUser
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

/**
 * The name of the IMSPermissionEntry GraphQL enum
 */
const val PERMISSION_ENTRY_NAME = "PermissionEntry"

@DomainNode
@GraphQLDescription(
    """Permission associated with a set of users.
    Can have SubPermissions to grant permissions on specific Nodes.
    """
)
class Permission(entries: MutableList<String>) : BasePermission(entries) {

    companion object {
        /**
         * Permission to check if a user can (globally) create [Project]s
         */
        const val CAN_CREATE_PROJECTS = "CAN_CREATE_PROJECTS"

        /**
         * Permission to check if a user can (globally) create [Component]s
         */
        const val CAN_CREATE_COMPONENTS = "CAN_CREATE_COMPONENTS"

        /**
         * Permission to check if a user can (globally) create [Template]s
         */
        const val CAN_CREATE_TEMPLATES = "CAN_CREATE_TEMPLATES"
    }

    @NodeRelationship(SubPermission.PART_OF, Direction.INCOMING)
    @GraphQLDescription("SubPermissions granting permissions on specific Nodes")
    @FilterProperty
    @delegate:Transient
    val subPermissions by NodeSetProperty<SubPermission<*>>()

    @NodeRelationship(GropiusUser.PERMISSION, Direction.INCOMING)
    @GraphQLDescription("GropiusUsers granted this Permission")
    @FilterProperty
    @delegate:Transient
    val users by NodeSetProperty<GropiusUser>()

    @GraphQLDescription(ENTRIES_DESCRIPTION)
    override val entries: MutableList<@TypeGraphQLType(PERMISSION_ENTRY_NAME) String>
        get() = super.entries
}