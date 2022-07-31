package gropius.repository.issue.timeline

import gropius.model.issue.timeline.ClosedEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ClosedEvent]
 */
@Repository
interface ClosedEventRepository : ReactiveNeo4jRepository<ClosedEvent, String>