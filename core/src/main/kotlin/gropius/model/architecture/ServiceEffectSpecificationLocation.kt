package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription(
    """Location which can be used with ServiceEffectSpecifications.
    Can be affected by Issues.
    """
)
abstract class ServiceEffectSpecificationLocation(name: String, description: String) : AffectedByIssue(
    name, description
) {

    companion object {
        const val INCOMING_SERVICE_EFFECT_SPECIFICATION = "INCOMING_SERVICE_EFFECT_SPECIFICATION"
        const val OUTGOING_SERVICE_EFFECT_SPECIFICATION = "OUTGOING_SERVICE_EFFECT_SPECIFICATION"
    }

    @NodeRelationship(INCOMING_SERVICE_EFFECT_SPECIFICATION, Direction.OUTGOING)
    @GraphQLDescription("ServiceEffectSpecifications where this is used in `incoming`.")
    @FilterProperty
    @delegate:Transient
    val incomingServiceEffectSpecifications by NodeSetProperty<ServiceEffectSpecification>()

    @NodeRelationship(OUTGOING_SERVICE_EFFECT_SPECIFICATION, Direction.OUTGOING)
    @GraphQLDescription("ServiceEffectSpecifications where this is used in `outgoing`.")
    @FilterProperty
    @delegate:Transient
    val outgoingServiceEffectSpecifications by NodeSetProperty<ServiceEffectSpecification>()
}