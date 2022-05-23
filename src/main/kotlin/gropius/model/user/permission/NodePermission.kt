package gropius.model.user.permission

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.authorization.NODE_PERMISSION_READ_RULE
import gropius.graphql.TypeGraphQLType
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

/**
 * Common base class for [IMSPermission], [ComponentPermission] and [ProjectPermission]
 *
 * @param T the type of Node which is affected by this permission
 * @param E the entry type, should be annotated with [TypeGraphQLType]
 */
@DomainNode
@GraphQLIgnore
@Authorization(NodePermission.READ, allow = [Rule(NODE_PERMISSION_READ_RULE)])
abstract class NodePermission<T : Node>(
    entries: MutableList<String>, allUsers: Boolean
) : BasePermission(entries, allUsers) {

    companion object {
        const val NODE = "NODE"

        /**
         * Permission to check if a user is allowed to read a node
         * This Permission is not implied by any other permission!
         */
        const val READ = "READ"

        /**
         * Permission to check if a user is allowed to administer a node
         * If [ADMIN] is granted, [READ] must be granted, too
         */
        const val ADMIN = "ADMIN"
    }

    @NodeRelationship(NODE, Direction.OUTGOING)
    @GraphQLDescription("Nodes on which the Permission is granted.")
    @FilterProperty
    @delegate:Transient
    val nodesWithPermission by NodeSetProperty<T>()

}