package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.model.common.ExtensibleNode
import gropius.model.template.BaseTemplate
import gropius.model.template.RelationTemplate
import gropius.model.template.TemplatedNode
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import org.springframework.data.neo4j.core.schema.CompositeProperty

@DomainNode
@GraphQLDescription(
    """A relation between RelationPartners (ComponentVersions and Interfaces).
    Relations are always directional.
    The template defines which RelationPartners are possible as start / end.
    For both start and end, if it is an Interface, it is possible to define the InterfaceParts this includes.
    Caution: This is **not** a supertype of IssueRelation.
    """
)
class Relation(
    @property:GraphQLIgnore
    @CompositeProperty
    override val templatedFields: MutableMap<String, String>
) : ExtensibleNode(), TemplatedNode {

    companion object {
        const val START_PART = "START_PART"
        const val END_PART = "END_PART"
    }

    @NodeRelationship(BaseTemplate.USED_IN, Direction.INCOMING)
    @GraphQLDescription("The Template of this Relation.")
    @FilterProperty
    @delegate:Transient
    val template by NodeProperty<RelationTemplate>()

    @NodeRelationship(RelationPartner.INCOMING_RELATION, Direction.INCOMING)
    @GraphQLDescription("The end of this Relation.")
    @FilterProperty
    @delegate:Transient
    var end by NodeProperty<RelationPartner>()

    @NodeRelationship(RelationPartner.OUTGOING_RELATION, Direction.INCOMING)
    @GraphQLDescription("The start of this Relation.")
    @FilterProperty
    @delegate:Transient
    var start by NodeProperty<RelationPartner>()

    @NodeRelationship(START_PART, Direction.OUTGOING)
    @GraphQLDescription("If the start is an Interface, the parts of that Interface this Relation includes.")
    @FilterProperty
    @delegate:Transient
    val startParts by NodeSetProperty<InterfacePart>()

    @NodeRelationship(END_PART, Direction.OUTGOING)
    @GraphQLDescription("If the end is an Interface, the parts of that Interface this Relation includes.")
    @FilterProperty
    @delegate:Transient
    val endParts by NodeSetProperty<InterfacePart>()
}