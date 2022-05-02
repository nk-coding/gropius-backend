package gropius.model.architecture

import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.net.URL

@DomainNode("projects")
class Project(name: String, description: String, repositoryURL: URL) : Trackable(name, description, repositoryURL) {

    companion object {
        const val COMPONENT = "COMPONENT"
    }

    @NodeRelationship(COMPONENT, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val components by NodeSetProperty<ComponentVersion>()
}