package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.SpentTimeChangedEvent

/**
 * Repository for [SpentTimeChangedEvent]
 */
@Repository
interface SpentTimeChangedEventRepository : ReactiveNeo4jRepository<SpentTimeChangedEvent, String>