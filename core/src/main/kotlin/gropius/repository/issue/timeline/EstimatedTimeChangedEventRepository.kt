package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.EstimatedTimeChangedEvent

/**
 * Repository for [EstimatedTimeChangedEvent]
 */
@Repository
interface EstimatedTimeChangedEventRepository : ReactiveNeo4jRepository<EstimatedTimeChangedEvent, String>