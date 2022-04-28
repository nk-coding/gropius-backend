package gropius.model.architecture

import io.github.graphglue.model.*
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
    val component by NodeProperty<ComponentVersion>()

    @NodeRelationship(SPECIFICATION, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val specification by NodeProperty<InterfaceSpecificationVersion>()
}