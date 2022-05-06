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
        const val IN_SERVICE_EFFECT_SPECIFICATION = "IN_SERVICE_EFFECT_SPECIFICATION"
        const val OUT_SERVICE_EFFECT_SPECIFICATION = "OUT_SERVICE_EFFECT_SPECIFICATION"
    }

    @NodeRelationship(IN_SERVICE_EFFECT_SPECIFICATION, Direction.OUTGOING)
    @GraphQLDescription("ServiceEffectSpecifications where this is used in `in`")
    @FilterProperty
    @delegate:Transient
    val inServiceEffectSpecifications by NodeSetProperty<ServiceEffectSpecification>()

    @NodeRelationship(OUT_SERVICE_EFFECT_SPECIFICATION, Direction.OUTGOING)
    @GraphQLDescription("ServiceEffectSpecifications where this is used in `out`")
    @FilterProperty
    @delegate:Transient
    val outServiceEffectSpecifications by NodeSetProperty<ServiceEffectSpecification>()
}