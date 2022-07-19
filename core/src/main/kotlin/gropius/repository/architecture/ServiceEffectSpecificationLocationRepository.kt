package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.ServiceEffectSpecificationLocation

/**
 * Repository for [ServiceEffectSpecificationLocation]
 */
@Repository
interface ServiceEffectSpecificationLocationRepository : ReactiveNeo4jRepository<ServiceEffectSpecificationLocation, String>