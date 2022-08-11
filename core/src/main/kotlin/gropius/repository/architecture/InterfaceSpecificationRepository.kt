package gropius.repository.architecture

import gropius.model.architecture.InterfaceSpecification
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [InterfaceSpecification]
 */
@Repository
interface InterfaceSpecificationRepository : ReactiveNeo4jRepository<InterfaceSpecification, String>