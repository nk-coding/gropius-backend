package gropius.repository.architecture

import gropius.model.architecture.Component
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Component]
 */
@Repository
interface ComponentRepository : ReactiveNeo4jRepository<Component, String>