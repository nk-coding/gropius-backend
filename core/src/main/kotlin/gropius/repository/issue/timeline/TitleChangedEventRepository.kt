package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.TitleChangedEvent

/**
 * Repository for [TitleChangedEvent]
 */
@Repository
interface TitleChangedEventRepository : ReactiveNeo4jRepository<TitleChangedEvent, String>