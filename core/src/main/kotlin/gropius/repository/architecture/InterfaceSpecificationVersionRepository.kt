package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.InterfaceSpecificationVersion

/**
 * Repository for [InterfaceSpecificationVersion]
 */
@Repository
interface InterfaceSpecificationVersionRepository : ReactiveNeo4jRepository<InterfaceSpecificationVersion, String>