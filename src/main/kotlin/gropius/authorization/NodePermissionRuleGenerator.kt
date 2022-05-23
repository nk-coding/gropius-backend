package gropius.authorization

import gropius.model.user.GropiusUser
import gropius.model.user.permission.BasePermission
import gropius.model.user.permission.NodePermission
import io.github.graphglue.authorization.AuthorizationRuleGenerator
import io.github.graphglue.authorization.Permission
import org.neo4j.cypherdsl.core.Condition
import org.neo4j.cypherdsl.core.Cypher
import org.neo4j.cypherdsl.core.Node
import org.neo4j.cypherdsl.core.Predicates

/**
 * Base class for all [NodePermission] based [AuthorizationRuleGenerator]s
 * Provides a method which is able to check for a specific permission on a [NodePermission] for a specific
 * [GropiusUser]
 */
interface NodePermissionRuleGenerator : AuthorizationRuleGenerator {

    /**
     * Generates the condition for the authorization
     * Is meant to be used in the where part of an existential subquery
     * If any permission in [permissionNames] is present, the condition evaluates to `true`
     *
     * @param nodePermissionNode the CypherDSL node of the [NodePermission] in the match pattern
     * @param gropiusUserNode the CypherDSL node of the [GropiusUser] in the match pattern
     * @param permission used to obtain the [GropiusAuthorizationContext] to check for the correct user
     * @param permissionNames list of permissions on
     */
    fun generatePredicateCondition(
        nodePermissionNode: Node, gropiusUserNode: Node, permission: Permission, permissionNames: List<String>
    ): Condition {
        val context = permission.context as GropiusAuthorizationContext
        val permissionVariable = Cypher.name("p")
        val nodePermissionPredicate = gropiusUserNode.property(BasePermission::allUsers.name).isTrue.or(
            Predicates.any(permissionVariable).`in`(Cypher.anonParameter(permissionNames))
                .where(permissionVariable.`in`(nodePermissionNode.property(BasePermission::entries.name)))
        )
        val gropiusUserPredicate = gropiusUserNode.property("id").isEqualTo(context.useridParameter)
        return nodePermissionPredicate.and(gropiusUserPredicate)
    }

}