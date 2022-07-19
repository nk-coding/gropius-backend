package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.ClosedEvent

/**
 * Repository for [ClosedEvent]
 */
@Repository
interface ClosedEventRepository : ReactiveNeo4jRepository<ClosedEvent, String>