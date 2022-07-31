package gropius.repository.issue.timeline

import gropius.model.issue.timeline.AddedToTrackableEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AddedToTrackableEvent]
 */
@Repository
interface AddedToTrackableEventRepository : ReactiveNeo4jRepository<AddedToTrackableEvent, String>