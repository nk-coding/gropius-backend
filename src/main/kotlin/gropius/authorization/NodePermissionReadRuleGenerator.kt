package gropius.authorization

import gropius.model.user.GropiusUser
import gropius.model.user.permission.NodePermission
import io.github.graphglue.authorization.Permission
import io.github.graphglue.definition.NodeDefinition
import io.github.graphglue.model.Rule
import org.neo4j.cypherdsl.core.Condition
import org.neo4j.cypherdsl.core.Cypher
import org.neo4j.cypherdsl.core.Node
import org.neo4j.cypherdsl.core.RelationshipPattern

/**
 * Permission rule generator used to check for READ on [NodePermission]
 * READ is granted if the user provided via the context is ADMIN on **any** related node.
 * For details how checking for ADMIN on the related node works, see [RelatedToNodePermissionRuleGenerator]
 *
 * @param nodePermissionDefinition used to generate Cypher DSL node for [NodePermission]
 * @param gropiusUserDefinition used to generate Cypher DSL node for [GropiusUser]
 */
class NodePermissionReadRuleGenerator(
    private val nodePermissionDefinition: NodeDefinition,
    private val gropiusUserDefinition: NodeDefinition
) : NodePermissionRuleGenerator {

    override fun generateRule(
        node: Node,
        currentRelationship: RelationshipPattern,
        rule: Rule,
        permission: Permission
    ): Pair<RelationshipPattern, Condition> {
        assert(permission.name == NodePermission.READ)

        val gropiusUserNode = gropiusUserDefinition.node().named("g_1")
        val relatedNodePermissionNode = nodePermissionDefinition.node().named("g_2")
        val subQueryPredicate = generatePredicateCondition(
            relatedNodePermissionNode, gropiusUserNode, permission, listOf(NodePermission.ADMIN)
        )
        val newRelationship = currentRelationship.relationshipBetween(relatedNodePermissionNode, NodePermission.NODE).length(0, 2)
            .relationshipFrom(gropiusUserNode, GropiusUser.PERMISSION)
        return newRelationship to subQueryPredicate
    }

}