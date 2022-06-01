package gropius.authorization

import gropius.model.architecture.*
import gropius.model.common.AuditedNode
import gropius.model.user.GropiusUser
import gropius.model.user.permission.*
import io.github.graphglue.authorization.AllowRuleGenerator
import io.github.graphglue.authorization.DisallowRuleGenerator
import io.github.graphglue.definition.NodeDefinition
import io.github.graphglue.definition.NodeDefinitionCollection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The name of the rule which checks that a Node is related to a [NodePermission]
 */
const val RELATED_TO_NODE_PERMISSION_RULE = "relatedToNodePermissionRule"

/**
 * The name of the [NodePermissionReadRuleGenerator] used to check for READ on  [NodePermission]
 */
const val NODE_PERMISSION_READ_RULE = "nodePermissionReadRule"

/**
 * The name of the [IsDeletedRuleGenerator] used to check that an [AuditedNode] is deleted
 */
const val IS_DELETED_RULE = "isDeletedRule"

/**
 * Provides Authorization related beans which are necessary to check for permissions
 */
@Configuration
class AuthorizationConfiguration {

    /**
     * Creates the [NodePermissionReadRuleGenerator] to check for READ on [NodePermission]
     * The rule can only be used on [NodePermission], and only for READ
     *
     * @param nodeDefinitionCollection used to obtain [NodeDefinition]s
     * @return the generated [NodePermissionReadRuleGenerator]
     */
    @Bean(NODE_PERMISSION_READ_RULE)
    fun nodePermissionReadRule(nodeDefinitionCollection: NodeDefinitionCollection): AllowRuleGenerator {
        return NodePermissionReadRuleGenerator(
            nodeDefinitionCollection.getNodeDefinition<NodePermission<*>>(),
            nodeDefinitionCollection.getNodeDefinition<GropiusUser>()
        )
    }

    /**
     * Creates the [IsDeletedRuleGenerator] to check that an [AuditedNode] is deleted
     *
     * @return the generated [IsDeletedRuleGenerator]
     */
    @Bean(IS_DELETED_RULE)
    fun isDeletedRule(): DisallowRuleGenerator = IsDeletedRuleGenerator()

    /**
     * Creates a new [RelatedToNodePermissionRuleGenerator] which can be used to check for permissions
     * on [IMS], [Component] and [Project]
     *
     * @param nodeDefinitionCollection used to get [NodeDefinition]s for [NodePermission] and [GropiusUser]
     * @return the generated [RelatedToNodePermissionRuleGenerator]
     */
    @Bean(RELATED_TO_NODE_PERMISSION_RULE)
    fun relatedToNodePermissionRule(nodeDefinitionCollection: NodeDefinitionCollection): RelatedToNodePermissionRuleGenerator {
        return RelatedToNodePermissionRuleGenerator(
            nodeDefinitionCollection.getNodeDefinition<NodePermission<*>>(),
            nodeDefinitionCollection.getNodeDefinition<GropiusUser>()
        )
    }

}