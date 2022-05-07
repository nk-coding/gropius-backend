package gropius.model.user

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.model.architecture.IMS
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription(
    """A user an IMS.
    This user might be linked to a GropiusUser.
    Note that this link can change at any time.
    The username might not be unique.
    It is possible that this user never heard of Gropius, and is only known to the system due to sync adapters.
    """
)
class IMSUser(
    displayName: String,
    email: String?,
    @GraphQLIgnore
    var username: String?
) : User(displayName, email) {

    companion object {
        const val GROPIUS_USER = "GROPIUS_USER"
    }

    @GraphQLDescription("The username of the IMSUser. Synced from the IMS this user is part of. Might not be unique.")
    override fun username(): String? = username

    @NodeRelationship(GROPIUS_USER, Direction.OUTGOING)
    @GraphQLDescription("The GropiusUser this IMSUser is linked to. An IMSUser might be linked to no GropiusUser.")
    @FilterProperty
    @delegate:Transient
    var gropiusUser by NodeProperty<GropiusUser?>()

    @NodeRelationship(IMS.USER, Direction.INCOMING)
    @GraphQLDescription("The IMS this user is part of.")
    @FilterProperty
    @delegate:Transient
    var ims by NodeProperty<IMS>()
}