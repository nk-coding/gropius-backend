package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.Body

/**
 * Repository for [Body]
 */
@Repository
interface BodyRepository : ReactiveNeo4jRepository<Body, String>