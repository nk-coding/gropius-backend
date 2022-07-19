package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.TypeChangedEvent

/**
 * Repository for [TypeChangedEvent]
 */
@Repository
interface TypeChangedEventRepository : ReactiveNeo4jRepository<TypeChangedEvent, String>