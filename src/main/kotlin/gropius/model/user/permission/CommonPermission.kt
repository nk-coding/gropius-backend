package gropius.model.user.permission

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.graphql.TypeGraphQLType
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.Node

/**
 * Common base class for [IMSPermission], [ComponentPermission] and [ProjectPermission]
 *
 * @param T the type of Node which is affected by this permission
 * @param E the entry type, should be annotated with [TypeGraphQLType]
 */
@DomainNode
@GraphQLIgnore
abstract class CommonPermission<T : Node>(entries: MutableList<String>) : SubPermission<T>(entries) {

    companion object {
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

}