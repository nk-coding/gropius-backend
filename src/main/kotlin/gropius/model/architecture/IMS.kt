package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.authorization.RELATED_TO_NODE_PERMISSION_RULE
import gropius.model.common.ExtensibleNode
import gropius.model.user.IMSUser
import gropius.model.user.permission.IMSPermission
import gropius.model.user.permission.NodePermission
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

@DomainNode("imss")
@GraphQLDescription(
    """Entity which represents an issue management system (like GitHub, Jira, Redmine, ...).
    Trackables can be added to this via an IMSProject, so that their issues are synced to this IMS.
    READ is granted via an associated IMSPermission.
    """
)
@Authorization(NodePermission.READ, allow = [Rule(RELATED_TO_NODE_PERMISSION_RULE)])
@Authorization(NodePermission.ADMIN, allow = [Rule(RELATED_TO_NODE_PERMISSION_RULE)])
@Authorization(IMSPermission.SYNC_TRACKABLES, allow = [Rule(RELATED_TO_NODE_PERMISSION_RULE, options = [NodePermission.ADMIN])])
class IMS : ExtensibleNode() {

    companion object {
        const val PROJECT = "PROJECT"
        const val USER = "USER"
    }

    @NodeRelationship(PROJECT, Direction.OUTGOING)
    @GraphQLDescription("Projects which are synced to this IMS.")
    @FilterProperty
    @delegate:Transient
    val projects by NodeSetProperty<IMSProject>()

    @NodeRelationship(USER, Direction.OUTGOING)
    @GraphQLDescription("Users of this IMS.")
    @FilterProperty
    @delegate:Transient
    val users by NodeSetProperty<IMSUser>()

    @NodeRelationship(NodePermission.NODE, Direction.INCOMING)
    @GraphQLDescription("Permissions for this IMS.")
    @FilterProperty
    @delegate:Transient
    val permissions by NodeSetProperty<IMSPermission>()

}