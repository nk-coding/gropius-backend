package gropius.model.common

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.model.user.User
import io.github.graphglue.model.*
import java.time.OffsetDateTime
import org.springframework.data.annotation.Transient

@DomainNode
abstract class SyncNode(
    @FilterProperty @OrderProperty val createdAt: OffsetDateTime,
    @FilterProperty @OrderProperty var lastModifiedAt: OffsetDateTime
) : ExtensibleNode() {

    companion object {
        const val CREATED_BY = "CREATED_BY"
        const val LAST_MODIFIED_BY = "LAST_MODIFIED_BY"
    }

    @GraphQLIgnore
    var isDeleted: Boolean = false

    @NodeRelationship(CREATED_BY, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var createdBy by NodeProperty<User>()

    @NodeRelationship(LAST_MODIFIED_BY, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var lastModifiedBy by NodeProperty<User>()

}