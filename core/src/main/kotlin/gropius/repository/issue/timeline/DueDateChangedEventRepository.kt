package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.DueDateChangedEvent

/**
 * Repository for [DueDateChangedEvent]
 */
@Repository
interface DueDateChangedEventRepository : ReactiveNeo4jRepository<DueDateChangedEvent, String>