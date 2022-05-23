package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.user.permission.NodePermission
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription(
    """Version of a component. 
    Can specifies visible/invisible InterfaceSpecifications.
    Can be used in Relations, affected by issues and included by Projects.
    READ is granted if READ is granted on `component`.
    """
)
@Authorization(NodePermission.READ, allowFromRelated = ["component"])
class ComponentVersion(
    name: String,
    description: String,
    @property:GraphQLDescription("The version of this ComponentVersion")
    override var version: String
) : RelationPartner(name, description), Versioned {

    companion object {
        const val VISIBLE_SELF_DEFINED = "VISIBLE_SELF_DEFINED"
        const val INVISIBLE_SELF_DEFINED = "INVISIBLE_SELF_DEFINED"
        const val VISIBLE_DERIVED = "VISIBLE_DERIVED"
        const val INVISIBLE_DERIVED = "INVISIBLE_DERIVED"
    }

    @NodeRelationship(Component.VERSION, Direction.INCOMING)
    @GraphQLDescription("The Component which defines this ComponentVersions")
    @FilterProperty
    @delegate:Transient
    val component by NodeProperty<Component>()

    @NodeRelationship(Interface.COMPONENT, Direction.INCOMING)
    @GraphQLDescription("Interfaces created by visible InterfaceSpecifications, can be used in Relations.")
    @FilterProperty
    @delegate:Transient
    val interfaces by NodeSetProperty<Interface>()

    @NodeRelationship(Project.COMPONENT, Direction.INCOMING)
    @GraphQLDescription("Projects which include this ComponentVersion")
    @FilterProperty
    @delegate:Transient
    val includingProjects by NodeSetProperty<Project>()

    @NodeRelationship(VISIBLE_SELF_DEFINED, Direction.OUTGOING)
    @GraphQLDescription(
        """InterfaceSpecifications which are defined by this Component(Version), and result in
        visible Interfaces on this ComponentVersion
        """
    )
    @FilterProperty
    @delegate:Transient
    val visibleSelfDefinedInterfaceSpecificationVersions by NodeSetProperty<InterfaceSpecificationVersion>()

    @NodeRelationship(INVISIBLE_SELF_DEFINED, Direction.OUTGOING)
    @GraphQLDescription(
        """InterfaceSpecifications which are defined by this Component(Version),
        but can only be inherited and do not result in Interfaces on this ComponentVersion
        """
    )
    @FilterProperty
    @delegate:Transient
    val invisibleSelfDefinedInterfaceSpecificationVersions by NodeSetProperty<InterfaceSpecificationVersion>()

    @NodeRelationship(VISIBLE_DERIVED, Direction.OUTGOING)
    @GraphQLDescription(
        """InterfaceSpecifications which are derived from other Components via a
        Relation and result in visible Interfaces on this ComponentVersion
        """
    )
    @FilterProperty
    @delegate:Transient
    val visibleDerivedInterfaceSpecificationVersions by NodeSetProperty<InterfaceSpecificationVersion>()

    @NodeRelationship(INVISIBLE_DERIVED, Direction.OUTGOING)
    @GraphQLDescription(
        """InterfaceSpecifications which are derived from other Components via a Relation,
        but can only be inherited and do not result in Interfaces on this ComponentVersion
        """
    )
    @FilterProperty
    @delegate:Transient
    val invisibleDerivedInterfaceSpecificationVersions by NodeSetProperty<InterfaceSpecificationVersion>()
}