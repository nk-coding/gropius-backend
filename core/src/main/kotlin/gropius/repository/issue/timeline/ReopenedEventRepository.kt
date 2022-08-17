package gropius.repository.issue.timeline

import gropius.model.issue.timeline.ReopenedEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ReopenedEvent]
 */
@Repository
interface ReopenedEventRepository : ReactiveNeo4jRepository<ReopenedEvent, String>