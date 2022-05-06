package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.common.ExtensibleNode
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription(
    """A relation between RelationPartners (ComponentVersions and Interfaces).
    Relations are always directional.
    The template defines which RelationPartners are possible as start / end.
    For both start and end, if it is an Interface, it is possible to define the InterfaceParts this includes.
    """
)
class Relation : ExtensibleNode() {

    companion object {
        const val START_PART = "START_PART"
        const val END_PART = "END_PART"
    }

    @NodeRelationship(RelationPartner.INGOING_RELATION, Direction.INCOMING)
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