package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.ComponentVersion

/**
 * Repository for [ComponentVersion]
 */
@Repository
interface ComponentVersionRepository : ReactiveNeo4jRepository<ComponentVersion, String>