package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.model.template.BaseTemplate
import gropius.model.template.InterfaceSpecificationTemplate
import gropius.model.template.TemplatedNode
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import org.springframework.data.neo4j.core.schema.CompositeProperty

@DomainNode
@GraphQLDescription(
    """Specification of an Interface.
    Defined on a Component, but can be visible and invisible on different ComponentVersions.
    Can be affected by Issues, and be used as start / end of ServiceEffectSpecifications.
    Defines InterfaceParts, but active parts depend on the InterfaceSpecificationVersion.
    """
)
class InterfaceSpecification(
    name: String,
    description: String,
    @property:GraphQLIgnore
    @CompositeProperty
    override val templatedFields: MutableMap<String, String>
) : ServiceEffectSpecificationLocation(
    name, description
), TemplatedNode {

    companion object {
        const val VERSION = "VERSION"
        const val COMPONENT = "COMPONENT"
    }

    @NodeRelationship(BaseTemplate.USED_IN, Direction.INCOMING)
    @GraphQLDescription("The Template of this InterfaceSpecification.")
    @FilterProperty
    @delegate:Transient
    val template by NodeProperty<InterfaceSpecificationTemplate>()

    @NodeRelationship(VERSION, Direction.OUTGOING)
    @GraphQLDescription("Versions of this InterfaceSpecification.")
    @FilterProperty
    @delegate:Transient
    val versions by NodeSetProperty<InterfaceSpecificationVersion>()

    @NodeRelationship(InterfacePart.DEFINED_ON, Direction.INCOMING)
    @GraphQLDescription(
        """InterfaceParts defined by this InterfaceSpecification.
        Note that active parts depend on the InterfaceSpecificationVersion
        """
    )
    @FilterProperty
    @delegate:Transient
    val definedParts by NodeSetProperty<InterfacePart>()

    @NodeRelationship(COMPONENT, Direction.OUTGOING)
    @GraphQLDescription("The Component this InterfaceSpecificaton is part of.")
    @FilterProperty
    @delegate:Transient
    var component by NodeProperty<Component>()
}