package gropius.model.user.permission

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.graphql.TypeGraphQLType
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.Node

/**
 * GraphQL description for permission entries
 */
const val ENTRIES_DESCRIPTION = "All permissions this Permission grants"

/**
 * Base class for all permissions
 *
 * @param entries the granted permission entries as Strings
 * @param E the entry type, should be annotated with [TypeGraphQLType]
 */
@DomainNode
@GraphQLIgnore
abstract class BasePermission(
    @GraphQLIgnore
    open val entries: MutableList<String>
) : Node()