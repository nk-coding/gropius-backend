package gropius.repository.architecture

import gropius.model.architecture.ComponentVersion
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ComponentVersion]
 */
@Repository
interface ComponentVersionRepository : ReactiveNeo4jRepository<ComponentVersion, String>