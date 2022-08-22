package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedFromTrackableEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedFromTrackableEvent]
 */
@Repository
interface RemovedFromTrackableEventRepository : GropiusRepository<RemovedFromTrackableEvent, String>