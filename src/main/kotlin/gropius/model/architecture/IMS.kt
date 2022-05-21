package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.common.ExtensibleNode
import gropius.model.user.IMSUser
import gropius.model.user.permission.IMSPermission
import gropius.model.user.permission.SubPermission
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode("imss")
@GraphQLDescription(
    """Entity which represents an issue management system (like GitHub, Jira, Redmine, ...).
    Trackables can be added to this via an IMSProject, so that their issues are synced to this IMS.
    """
)
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

    @NodeRelationship(SubPermission.NODE, Direction.INCOMING)
    @GraphQLDescription("Permissions for this IMS.")
    @FilterProperty
    @delegate:Transient
    val permissions by NodeSetProperty<IMSPermission>()

}