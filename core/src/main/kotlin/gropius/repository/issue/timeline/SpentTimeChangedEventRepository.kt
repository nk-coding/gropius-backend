package gropius.repository.issue.timeline

import gropius.model.issue.timeline.SpentTimeChangedEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [SpentTimeChangedEvent]
 */
@Repository
interface SpentTimeChangedEventRepository : ReactiveNeo4jRepository<SpentTimeChangedEvent, String>