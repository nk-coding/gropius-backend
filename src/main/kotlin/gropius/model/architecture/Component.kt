package gropius.model.architecture

import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient
import java.net.URL

@DomainNode("components")
class Component(name: String, description: String, repositoryURL: URL) : Trackable(name, description, repositoryURL) {

    companion object {
        const val VERSION = "VERSION"
    }

    @NodeRelationship(InterfaceSpecification.COMPONENT, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val interfaceSpecifications by NodeSetProperty<InterfaceSpecification>()

    @NodeRelationship(VERSION, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val versions by NodeSetProperty<ComponentVersion>()

}