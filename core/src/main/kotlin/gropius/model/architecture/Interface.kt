package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.user.permission.NodePermission
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

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
    description: String
) : RelationPartner(name, description) {

    companion object {
        const val DEFINITION = "DEFINITION"
    }

    @NodeRelationship(DEFINITION, Direction.OUTGOING)
    @GraphQLDescription("The definition of this interface.")
    @FilterProperty
    @delegate:Transient
    val interfaceDefinition by NodeProperty<InterfaceDefinition>()
}