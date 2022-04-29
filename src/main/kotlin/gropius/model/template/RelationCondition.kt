package gropius.model.template

import gropius.model.common.ExtensibleNode
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

@DomainNode
class RelationCondition(
    @FilterProperty val inheritsVisibleSelfDefined: Boolean,
    @FilterProperty val inheritsInvisibleSelfDefined: Boolean,
    @FilterProperty val inheritsVisibleDerived: Boolean,
    @FilterProperty val inheritsInvisibleDerived: Boolean,
    @FilterProperty val isVisibleInherited: Boolean
) : ExtensibleNode() {

    companion object {
        const val PART_OF = "PART_OF"
        const val FROM = "FROM"
        const val TO = "TO"
    }

    @NodeRelationship(InterfaceSpecificationInheritanceCondition.PART_OF, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val interfaceSpecificationInheritanceConditions by NodeSetProperty<InterfaceSpecificationInheritanceCondition>()

    @NodeRelationship(PART_OF, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val partOf by NodeSetProperty<RelationTemplate>()

    @NodeRelationship(FROM, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val from by NodeSetProperty<RelationPartnerTemplate<*,*>>()

    @NodeRelationship(TO, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val to by NodeSetProperty<RelationPartnerTemplate<*,*>>()

}