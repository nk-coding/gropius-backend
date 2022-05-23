package gropius.authorization

import gropius.model.architecture.*
import gropius.model.user.GropiusUser
import gropius.model.user.permission.*
import io.github.graphglue.authorization.AuthorizationRuleGenerator
import io.github.graphglue.definition.NodeDefinition
import io.github.graphglue.definition.NodeDefinitionCollection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The name of the NodePermission rule which can be used on [IMS]
 */
const val IMS_PERMISSION_RULE = "imsPermissionRule"

/**
 * The name of the TrackablePermission rule which can be used on [Trackable]
 */
const val TRACKABLE_PERMISSION_RULE = "trackablePermissionRule"

/**
 * The name of the NodePermission rule which can be used on [Component]
 */
const val COMPONENT_PERMISSION_RULE = "componentPermissionRule"

/**
 * The name of the NodePermission rule which can be used on [Project]
 */
const val PROJECT_PERMISSION_RULE = "projectPermissionRule"

/**
 * The name of the [NodePermissionReadRuleGenerator] used to check for READ on  [NodePermission]
 */
const val NODE_PERMISSION_READ_RULE = "nodePermissionReadRule"

/**
 * The name of the [ComponentReadViaProjectRuleGenerator] used to check for READ on [Component]
 */
const val COMPONENT_READ_VIA_PROJECT_RULE = "componentReadViaProjectRule"

/**
 * Provides Authorization related beans which are necessary to check for permissions
 */
@Configuration
class AuthorizationConfiguration {

    /**
     * Creates a [RelatedToNodePermissionRuleGenerator] for [IMSPermission]
     * The rule can be used on [IMS]
     *
     * @param nodeDefinitionCollection used to obtain [NodeDefinition]s
     * @return the generated [RelatedToNodePermissionRuleGenerator]
     */
    @Bean(IMS_PERMISSION_RULE)
    fun imsPermissionRule(nodeDefinitionCollection: NodeDefinitionCollection): AuthorizationRuleGenerator {
        return generateNodePermissionRuleGenerator(
            nodeDefinitionCollection, nodeDefinitionCollection.getNodeDefinition<IMSPermission>()
        )
    }

    /**
     * Creates a [RelatedToNodePermissionRuleGenerator] for [TrackablePermission]
     * The rule can be used on [Trackable]
     *
     * @param nodeDefinitionCollection used to obtain [NodeDefinition]s
     * @return the generated [RelatedToNodePermissionRuleGenerator]
     */
    @Bean(TRACKABLE_PERMISSION_RULE)
    fun trackablePermissionRule(nodeDefinitionCollection: NodeDefinitionCollection): AuthorizationRuleGenerator {
        return generateNodePermissionRuleGenerator(
            nodeDefinitionCollection, nodeDefinitionCollection.getNodeDefinition<TrackablePermission<*>>()
        )
    }

    /**
     * Creates a [RelatedToNodePermissionRuleGenerator] for [ComponentPermission]
     * The rule can be used on [Component]
     *
     * @param nodeDefinitionCollection used to obtain [NodeDefinition]s
     * @return the generated [RelatedToNodePermissionRuleGenerator]
     */
    @Bean(COMPONENT_PERMISSION_RULE)
    fun componentPermissionRule(nodeDefinitionCollection: NodeDefinitionCollection): AuthorizationRuleGenerator {
        return generateNodePermissionRuleGenerator(
            nodeDefinitionCollection, nodeDefinitionCollection.getNodeDefinition<ComponentPermission>()
        )
    }

    /**
     * Creates a [RelatedToNodePermissionRuleGenerator] for [ProjectPermission]
     * The rule can be used on [Project]
     *
     * @param nodeDefinitionCollection used to obtain [NodeDefinition]s
     * @return the generated [RelatedToNodePermissionRuleGenerator]
     */
    @Bean(PROJECT_PERMISSION_RULE)
    fun projectPermissionRule(nodeDefinitionCollection: NodeDefinitionCollection): AuthorizationRuleGenerator {
        return generateNodePermissionRuleGenerator(
            nodeDefinitionCollection, nodeDefinitionCollection.getNodeDefinition<ProjectPermission>()
        )
    }

    /**
     * Creates the [NodePermissionReadRuleGenerator] to check for READ on [NodePermission]
     * The rule can only be used on [NodePermission], and only for READ
     *
     * @param nodeDefinitionCollection used to obtain [NodeDefinition]s
     * @return the generated [NodePermissionReadRuleGenerator]
     */
    @Bean(NODE_PERMISSION_READ_RULE)
    fun nodePermissionReadRule(nodeDefinitionCollection: NodeDefinitionCollection): AuthorizationRuleGenerator {
        return NodePermissionReadRuleGenerator(
            nodeDefinitionCollection.getNodeDefinition<NodePermission<*>>(),
            nodeDefinitionCollection.getNodeDefinition<GropiusUser>()
        )
    }

    /**
     * Creates the [ComponentReadViaProjectRuleGenerator] to check for READ on [Component]
     * The rule can only be used on [Component], and only for READ
     * Should always be used in combination with `Rule(COMPONENT_PERMISSION_RULE)]`
     *
     * @param nodeDefinitionCollection used to obtain [NodeDefinition]s
     * @return the generated [ComponentReadViaProjectRuleGenerator]
     */
    @Bean(COMPONENT_READ_VIA_PROJECT_RULE)
    fun componentReadViaProjectRule(nodeDefinitionCollection: NodeDefinitionCollection): AuthorizationRuleGenerator {
        return ComponentReadViaProjectRuleGenerator(
            nodeDefinitionCollection.getNodeDefinition<ComponentVersion>(),
            nodeDefinitionCollection.getNodeDefinition<Project>(),
            nodeDefinitionCollection.getNodeDefinition<ProjectPermission>(),
            nodeDefinitionCollection.getNodeDefinition<GropiusUser>()
        )
    }

    /**
     * Creates a new [RelatedToNodePermissionRuleGenerator] based on the provided [nodePermissionDefinition]
     *
     * @param nodeDefinitionCollection used to get [NodeDefinition]s for [GlobalPermission] and [GropiusUser]
     * @param nodePermissionDefinition the [NodeDefinition] of the specific subtype of [NodePermission]
     * @return the generated [RelatedToNodePermissionRuleGenerator]
     */
    private fun generateNodePermissionRuleGenerator(
        nodeDefinitionCollection: NodeDefinitionCollection, nodePermissionDefinition: NodeDefinition
    ): RelatedToNodePermissionRuleGenerator {
        return RelatedToNodePermissionRuleGenerator(
            nodePermissionDefinition, nodeDefinitionCollection.getNodeDefinition<GropiusUser>()
        )
    }

}