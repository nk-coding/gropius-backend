package gropius.repository.issue.timeline

import gropius.model.issue.timeline.TitleChangedEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [TitleChangedEvent]
 */
@Repository
interface TitleChangedEventRepository : ReactiveNeo4jRepository<TitleChangedEvent, String>