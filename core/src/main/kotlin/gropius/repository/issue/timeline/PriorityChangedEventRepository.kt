package gropius.repository.issue.timeline

import gropius.model.issue.timeline.PriorityChangedEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [PriorityChangedEvent]
 */
@Repository
interface PriorityChangedEventRepository : ReactiveNeo4jRepository<PriorityChangedEvent, String>