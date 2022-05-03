package gropius.model.architecture

import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
class Interface(name: String, description: String) : RelationPartner(name, description) {

    companion object {
        const val COMPONENT = "COMPONENT"
        const val SPECIFICATION = "SPECIFICATION"
    }

    @NodeRelationship(COMPONENT, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var component by NodeProperty<ComponentVersion>()

    @NodeRelationship(SPECIFICATION, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var specification by NodeProperty<InterfaceSpecificationVersion>()
}