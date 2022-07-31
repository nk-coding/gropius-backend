package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedFromTrackableEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedFromTrackableEvent]
 */
@Repository
interface RemovedFromTrackableEventRepository : ReactiveNeo4jRepository<RemovedFromTrackableEvent, String>