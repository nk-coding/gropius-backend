package gropius.model.template

import gropius.model.common.ExtensibleNode
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient


@DomainNode
class InterfaceSpecificationInheritanceCondition : ExtensibleNode() {

    companion object {
        const val PART_OF = "PART_OF"
    }

    @NodeRelationship(PART_OF, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var partOf by NodeProperty<RelationCondition>()
}