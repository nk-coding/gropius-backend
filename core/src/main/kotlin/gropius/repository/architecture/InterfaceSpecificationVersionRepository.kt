package gropius.repository.architecture

import gropius.model.architecture.InterfaceSpecificationVersion
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [InterfaceSpecificationVersion]
 */
@Repository
interface InterfaceSpecificationVersionRepository : ReactiveNeo4jRepository<InterfaceSpecificationVersion, String>