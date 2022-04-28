package gropius.model.architecture

import gropius.model.common.NamedNode
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

@DomainNode
class ServiceEffectSpecification(name: String, description: String) : NamedNode(name, description) {

    @NodeRelationship(ServiceEffectSpecificationLocation.IN_SERVICE_EFFECT_SPECIFICATION, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val `in` by NodeSetProperty<ServiceEffectSpecificationLocation>()

    @NodeRelationship(ServiceEffectSpecificationLocation.OUT_SERVICE_EFFECT_SPECIFICATION, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val out by NodeSetProperty<ServiceEffectSpecificationLocation>()

}