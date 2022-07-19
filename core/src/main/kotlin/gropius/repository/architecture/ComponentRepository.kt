package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.Component

/**
 * Repository for [Component]
 */
@Repository
interface ComponentRepository : ReactiveNeo4jRepository<Component, String>