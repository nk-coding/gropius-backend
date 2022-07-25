package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.model.template.BaseTemplate
import gropius.model.template.InterfaceTemplate
import gropius.model.template.MutableTemplatedNode
import gropius.model.user.permission.NodePermission
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient
import org.springframework.data.neo4j.core.schema.CompositeProperty

@DomainNode
@GraphQLDescription(
    """An interface which is part of a specific ComponentVersion.
    Its semantics depend on the InterfaceSpecification it is specified by, e.g. an Interface can represent a REST API.
    Can be used in Relations and affected by Issues.
    READ is granted if READ is granted on `interfaceDefinition`.
    """
)
@Authorization(NodePermission.READ, allowFromRelated = ["interfaceDefinition"])
class Interface(
    name: String,
    description: String,
    @property:GraphQLIgnore
    @CompositeProperty
    override val templatedFields: MutableMap<String, String>
) : RelationPartner(name, description), MutableTemplatedNode {

    companion object {
        const val DEFINITION = "DEFINITION"
    }

    @NodeRelationship(BaseTemplate.USED_IN, Direction.INCOMING)
    @GraphQLDescription("The Template of this Interface.")
    @FilterProperty
    @delegate:Transient
    override val template by NodeProperty<InterfaceTemplate>()

    @NodeRelationship(DEFINITION, Direction.OUTGOING)
    @GraphQLDescription("The definition of this interface.")
    @FilterProperty
    @delegate:Transient
    val interfaceDefinition by NodeProperty<InterfaceDefinition>()
}