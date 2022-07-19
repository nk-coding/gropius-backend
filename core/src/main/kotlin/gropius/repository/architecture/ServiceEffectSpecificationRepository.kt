package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.ServiceEffectSpecification

/**
 * Repository for [ServiceEffectSpecification]
 */
@Repository
interface ServiceEffectSpecificationRepository : ReactiveNeo4jRepository<ServiceEffectSpecification, String>