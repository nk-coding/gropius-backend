package gropius.model.common

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.model.user.User
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

@DomainNode
@GraphQLDescription(
    """ExtensibleNode which provides information necessary for sync
    When it was created and last modified, if the it is already deleted, and by who it was created and last modiied.
    """
)
abstract class SyncNode(
    @GraphQLDescription("The DateTime this entity was created at.")
    @FilterProperty
    @OrderProperty
    val createdAt: OffsetDateTime,
    @GraphQLDescription("The DateTime this entity was last modified at.")
    @FilterProperty
    @OrderProperty
    var lastModifiedAt: OffsetDateTime
) : ExtensibleNode() {

    companion object {
        const val CREATED_BY = "CREATED_BY"
        const val LAST_MODIFIED_BY = "LAST_MODIFIED_BY"
    }

    /**
     * If true, this entity is marked as deleted and should never be returned by the API.
     * However, it cannot be deleted completely as is still may be synced.
     */
    @GraphQLIgnore
    var isDeleted: Boolean = false

    @NodeRelationship(CREATED_BY, Direction.OUTGOING)
    @GraphQLDescription("The User who created this entity.")
    @FilterProperty
    @delegate:Transient
    var createdBy by NodeProperty<User>()

    @NodeRelationship(LAST_MODIFIED_BY, Direction.OUTGOING)
    @GraphQLDescription("The User who last modified this entity.")
    @FilterProperty
    @delegate:Transient
    var lastModifiedBy by NodeProperty<User>()

}