package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.common.NamedNode
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription(
    """Describes a dependency between InterfaceSpecification(Version)s
    Semantically, any InterfaceSpecification(Version) in `out` depends on any InterfaceSpecification(Version) in `in`.
    This can result in a propagation of Issues, if any location in `in` is in some regard affected by an Issue,
    all locations in `out` are affected by this Issue, too.
    """
)
class ServiceEffectSpecification(name: String, description: String) : NamedNode(name, description) {

    @NodeRelationship(ServiceEffectSpecificationLocation.INCOMING_SERVICE_EFFECT_SPECIFICATION, Direction.INCOMING)
    @GraphQLDescription("The incoming Interfaces of this ServiceEffectSpecification.")
    @FilterProperty
    @delegate:Transient
    val incoming by NodeSetProperty<ServiceEffectSpecificationLocation>()

    @NodeRelationship(ServiceEffectSpecificationLocation.OUTGOING_SERVICE_EFFECT_SPECIFICATION, Direction.INCOMING)
    @GraphQLDescription("The outgoing Interfaces of this ServiceEffectSpecification.")
    @FilterProperty
    @delegate:Transient
    val outgoing by NodeSetProperty<ServiceEffectSpecificationLocation>()

}