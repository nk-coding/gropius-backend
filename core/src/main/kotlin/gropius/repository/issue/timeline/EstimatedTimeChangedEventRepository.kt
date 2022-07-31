package gropius.repository.issue.timeline

import gropius.model.issue.timeline.EstimatedTimeChangedEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [EstimatedTimeChangedEvent]
 */
@Repository
interface EstimatedTimeChangedEventRepository : ReactiveNeo4jRepository<EstimatedTimeChangedEvent, String>