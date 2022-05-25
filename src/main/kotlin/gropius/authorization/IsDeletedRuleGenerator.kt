package gropius.authorization

import gropius.model.common.AuditedNode
import io.github.graphglue.authorization.DisallowRuleGenerator
import io.github.graphglue.authorization.Permission
import io.github.graphglue.model.Rule
import org.neo4j.cypherdsl.core.Condition
import org.neo4j.cypherdsl.core.Node

/**
 * Checks that a [AuditedNode] is deleted.
 * Mean to be used in the disallow-part
 */
class IsDeletedRuleGenerator : DisallowRuleGenerator {

    override fun generateRule(node: Node, rule: Rule, permission: Permission): Condition {
        return node.property(AuditedNode::isDeleted.name).isTrue
    }
}