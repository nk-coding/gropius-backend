package gropius.repository.common

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.common.Named

/**
 * Repository for [Named]
 */
@Repository
interface NamedRepository : ReactiveNeo4jRepository<Named, String>