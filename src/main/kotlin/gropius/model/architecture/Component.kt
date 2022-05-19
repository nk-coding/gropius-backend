package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.net.URI

@DomainNode("components")
@GraphQLDescription(
    """Entity which represents a software component, e.g. a library, a microservice, or a deployment platform, ....
    The type of software component is defined by the template.
    Can have issues, labels and artefacts as this is a Trackable.
    Defines InterfaceSpecifications, but visible/invisible InterfaceSpecificationVersions depend on the ComponentVersion.
    Can be affected by Issues.
    """
)
class Component(name: String, description: String, repositoryURL: URI) : Trackable(name, description, repositoryURL) {

    companion object {
        const val VERSION = "VERSION"
    }

    @NodeRelationship(InterfaceSpecification.COMPONENT, Direction.INCOMING)
    @GraphQLDescription(
        """List of interfaces this component specifies.
        Note that visible/invisible InterfaceSpecifications are defined by a specific version of this component
        """
    )
    @FilterProperty
    @delegate:Transient
    val interfaceSpecifications by NodeSetProperty<InterfaceSpecification>()

    @NodeRelationship(VERSION, Direction.OUTGOING)
    @GraphQLDescription("Versions of this components.")
    @FilterProperty
    @delegate:Transient
    val versions by NodeSetProperty<ComponentVersion>()

}