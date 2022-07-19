package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.ReopenedEvent

/**
 * Repository for [ReopenedEvent]
 */
@Repository
interface ReopenedEventRepository : ReactiveNeo4jRepository<ReopenedEvent, String>