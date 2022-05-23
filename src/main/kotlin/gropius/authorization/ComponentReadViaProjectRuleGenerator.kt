package gropius.authorization

import gropius.model.architecture.Component
import gropius.model.architecture.ComponentVersion
import gropius.model.architecture.Project
import gropius.model.user.GropiusUser
import gropius.model.user.permission.ComponentPermission
import gropius.model.user.permission.NodePermission
import gropius.model.user.permission.ProjectPermission
import io.github.graphglue.authorization.Permission
import io.github.graphglue.definition.NodeDefinition
import io.github.graphglue.model.Rule
import org.neo4j.cypherdsl.core.Condition
import org.neo4j.cypherdsl.core.Cypher
import org.neo4j.cypherdsl.core.Node

/**
 * Permission rule generator used to check for READ on [Component]
 * It evaluates to `true` if any version of the [Component] is included in a [Project], for
 * which READ is granted.
 * This should always be used in combination with a Rule that checks for direct READ due to a [ComponentPermission]
 *
 * @param componentVersionDefinition used to generate Cypher DSL node for [ComponentVersion]
 * @param projectDefinition used to generate Cypher DSL node for [Project]
 * @param projectPermissionDefinition used to generate Cypher DSL node for [ProjectPermission]
 * @param gropiusUserDefinition used to generate Cypher DSL node for [GropiusUser]
 */
class ComponentReadViaProjectRuleGenerator(
    private val componentVersionDefinition: NodeDefinition,
    private val projectDefinition: NodeDefinition,
    private val projectPermissionDefinition: NodeDefinition,
    private val gropiusUserDefinition: NodeDefinition
) : NodePermissionRuleGenerator {

    override fun generateCondition(node: Node, rule: Rule, permission: Permission): Condition {
        assert(permission.name == NodePermission.READ)

        val componentVersionNode = componentVersionDefinition.node().named("g_1")
        val projectNode = projectDefinition.node().named("g_2")
        val projectPermissionNode = projectPermissionDefinition.node().named("g_3")
        val gropiusUserNode = gropiusUserDefinition.node().named("g_4")
        val subQueryPredicate = generatePredicateCondition(
            projectPermissionNode, gropiusUserNode, permission, listOf(NodePermission.READ)
        )
        return Cypher.match(
            node.relationshipTo(componentVersionNode, Component.VERSION)
                .relationshipFrom(projectNode, Project.COMPONENT)
                .relationshipFrom(projectPermissionNode, NodePermission.NODE)
                .relationshipFrom(gropiusUserNode, GropiusUser.PERMISSION)
        ).where(subQueryPredicate).asCondition()
    }
}