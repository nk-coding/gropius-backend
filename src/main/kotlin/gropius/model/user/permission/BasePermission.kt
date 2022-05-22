package gropius.model.user.permission

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.model.user.GropiusUser
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

/**
 * GraphQL description for permission entries
 */
const val ENTRIES_DESCRIPTION = "All permissions this Permission grants"

/**
 * Base class for all permissions
 *
 * @param entries the granted permission entries as Strings
 */
@DomainNode
@GraphQLIgnore
abstract class BasePermission(
    @GraphQLIgnore
    open val entries: MutableList<String>
) : Node() {

    @NodeRelationship(GropiusUser.PERMISSION, Direction.INCOMING)
    @GraphQLDescription("GropiusUsers granted this Permission")
    @FilterProperty
    @delegate:Transient
    val users by NodeSetProperty<GropiusUser>()

}