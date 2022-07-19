package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.InterfaceSpecification

/**
 * Repository for [InterfaceSpecification]
 */
@Repository
interface InterfaceSpecificationRepository : ReactiveNeo4jRepository<InterfaceSpecification, String>