package gropius.repository.issue.timeline

import gropius.model.issue.timeline.Body
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Body]
 */
@Repository
interface BodyRepository : ReactiveNeo4jRepository<Body, String>