package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.RemovedFromTrackableEvent

/**
 * Repository for [RemovedFromTrackableEvent]
 */
@Repository
interface RemovedFromTrackableEventRepository : ReactiveNeo4jRepository<RemovedFromTrackableEvent, String>