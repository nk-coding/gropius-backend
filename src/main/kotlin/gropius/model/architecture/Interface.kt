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
    READ is granted if READ is granted on `component`.
    """
)
@Authorization(NodePermission.READ, allowFromRelated = ["component"])
class Interface(name: String, description: String) : RelationPartner(name, description) {

    companion object {
        const val COMPONENT = "COMPONENT"
        const val SPECIFICATION = "SPECIFICATION"
    }

    @NodeRelationship(COMPONENT, Direction.OUTGOING)
    @GraphQLDescription("The ComponentVersion this Interface is part of.")
    @FilterProperty
    @delegate:Transient
    val component by NodeProperty<ComponentVersion>()

    @NodeRelationship(SPECIFICATION, Direction.OUTGOING)
    @GraphQLDescription("The InterfaceSpecification which specifies this Interface and thereby defines its semantics.")
    @FilterProperty
    @delegate:Transient
    val specification by NodeProperty<InterfaceSpecificationVersion>()
}