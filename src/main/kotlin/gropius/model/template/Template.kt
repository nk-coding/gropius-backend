package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.common.NamedNode
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

// TODO templateFieldSpecifications
@DomainNode
@GraphQLDescription(
    """Template for a specific type of Node.
    Defines templated fields with specific types (defined using JSON schema).
    """
)
abstract class Template<T : Node, S : Template<T, S>>(
    name: String,
    description: String,
    @GraphQLDescription("If true, this template is deprecated and cannot be used for new entities any more.")
    @FilterProperty
    var isDeprecated: Boolean
) : NamedNode(name, description) {

    companion object {
        const val USED_IN = "USED_IN"
        const val EXTENDS = "EXTENDS"
    }

    @NodeRelationship(USED_IN, Direction.OUTGOING)
    @GraphQLDescription("Entities which use this template.")
    @delegate:Transient
    val usedIn by NodeSetProperty<T>()

    @NodeRelationship(EXTENDS, Direction.OUTGOING)
    @GraphQLDescription("Template this template extends.")
    @delegate:Transient
    val extends by NodeSetProperty<S>()

    @NodeRelationship(EXTENDS, Direction.INCOMING)
    @GraphQLDescription("Templates that extend this template.")
    @delegate:Transient
    val extendedBy by NodeSetProperty<S>()
}