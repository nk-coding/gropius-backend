package gropius.repository.issue.timeline

import gropius.model.issue.timeline.StartDateChangedEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [StartDateChangedEvent]
 */
@Repository
interface StartDateChangedEventRepository : ReactiveNeo4jRepository<StartDateChangedEvent, String>