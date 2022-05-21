package gropius.model.user.permission

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.graphql.TypeGraphQLType
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

/**
 * SubPermission with is part of a specific Permission and grants permissions to specific nodes of type T
 *
 * @param T the type of Node which is affected by this permission
 */
@DomainNode
@GraphQLIgnore
abstract class SubPermission<T : Node>(entries: MutableList<String>) : BasePermission(entries) {

    companion object {
        const val PART_OF = "PART_OF"
        const val NODE = "NODE"
    }

    @NodeRelationship(PART_OF, Direction.OUTGOING)
    @GraphQLDescription("The Permission this is a part of.")
    @FilterProperty
    @delegate:Transient
    val partOf by NodeProperty<Permission>()

    @NodeRelationship(NODE, Direction.OUTGOING)
    @GraphQLDescription("Nodes on which the Permission is granted.")
    @FilterProperty
    @delegate:Transient
    val nodesWithPermission by NodeSetProperty<T>()
}