package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.Versioned

/**
 * Repository for [Versioned]
 */
@Repository
interface VersionedRepository : ReactiveNeo4jRepository<Versioned, String>