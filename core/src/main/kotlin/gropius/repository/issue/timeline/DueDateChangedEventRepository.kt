package gropius.repository.issue.timeline

import gropius.model.issue.timeline.DueDateChangedEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [DueDateChangedEvent]
 */
@Repository
interface DueDateChangedEventRepository : ReactiveNeo4jRepository<DueDateChangedEvent, String>