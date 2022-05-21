package gropius.model.user

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.model.user.permission.Permission
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription(
    """A user of the Gropius System.
    The username can be used as unique identifier for GropiusUsers.
    IMSUsers can be linked to a GropiusUser.
    Note however that this link does not affect relationships, e.g. if an IMSUser is part of an Assignment,
    after the IMSUser was linked to a GropiusUser, the GropiusUser does not link directly to the Assignment.
    Therefore, to collect all Assignments, Issue participations etc. it is necessary to also request all 
    linked IMSUsers and their Assignments etc.
    """
)
class GropiusUser(
    displayName: String,
    email: String?,
    @property:GraphQLIgnore
    val username: String
) : User(displayName, email) {

    companion object {
        const val PERMISSION = "PERMISSION"
    }

    @GraphQLDescription("A unique identifier for the GropiusUser. Note that this might not be unique across all Users.")
    override fun username(): String = username

    @NodeRelationship(IMSUser.GROPIUS_USER, Direction.INCOMING)
    @GraphQLDescription("The IMSUsers linked to this GropiusUser.")
    @FilterProperty
    @delegate:Transient
    val imsUsers by NodeSetProperty<IMSUser>()

    @NodeRelationship(PERMISSION, Direction.OUTGOING)
    @GraphQLDescription("Permissions the user has.")
    @FilterProperty
    @delegate:Transient
    val permissions by NodeSetProperty<Permission>()
}