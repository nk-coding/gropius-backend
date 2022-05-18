package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.model.template.BaseTemplate
import gropius.model.template.InterfacePartTemplate
import gropius.model.template.TemplatedNode
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import org.springframework.data.neo4j.core.schema.CompositeProperty

@DomainNode
@GraphQLDescription(
    """Part of an Interface(Specification).
    Its semantics depend on the InterfaceSpecification, e.g. for a REST API interface,
    this could represent a single endpoint of the API.
    Relations can specify for both start and end included InterfaceParts.
    Can be affected by Issues, and be used as start / end of ServiceEffectSpecifications.
    """
)
class InterfacePart(
    name: String,
    description: String,
    @property:GraphQLIgnore
    @CompositeProperty
    override val templatedFields: MutableMap<String, String>
) : ServiceEffectSpecificationLocation(name, description), TemplatedNode {

    companion object {
        const val DEFINED_ON = "DEFINED_ON"
    }

    @NodeRelationship(BaseTemplate.USED_IN, Direction.INCOMING)
    @GraphQLDescription("The Template of this InterfacePart")
    @FilterProperty
    @delegate:Transient
    val template by NodeProperty<InterfacePartTemplate>()

    @NodeRelationship(Relation.START_PART, Direction.INCOMING)
    @GraphQLDescription("Relations which include this InterfacePart at the start of the Relation")
    @FilterProperty
    @delegate:Transient
    val includingOutgoingRelations by NodeSetProperty<Relation>()

    @NodeRelationship(Relation.END_PART, Direction.INCOMING)
    @GraphQLDescription("Relations which include this InterfacePart at the end of the Relation")
    @FilterProperty
    @delegate:Transient
    val includingIncomingRelations by NodeSetProperty<Relation>()

    @NodeRelationship(InterfaceSpecificationVersion.ACTIVE_PART, Direction.INCOMING)
    @GraphQLDescription("InterfaceSpecificationVersions where this InterfacePart is active.")
    @FilterProperty
    @delegate:Transient
    val activeOn by NodeSetProperty<InterfaceSpecificationVersion>()

    @NodeRelationship(DEFINED_ON, Direction.OUTGOING)
    @GraphQLDescription("InterfaceSpecification which defines this InterfacePart")
    @FilterProperty
    @delegate:Transient
    var definedOn by NodeProperty<InterfaceSpecification>()

}