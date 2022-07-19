package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.StartDateChangedEvent

/**
 * Repository for [StartDateChangedEvent]
 */
@Repository
interface StartDateChangedEventRepository : ReactiveNeo4jRepository<StartDateChangedEvent, String>