package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.PriorityChangedEvent

/**
 * Repository for [PriorityChangedEvent]
 */
@Repository
interface PriorityChangedEventRepository : ReactiveNeo4jRepository<PriorityChangedEvent, String>