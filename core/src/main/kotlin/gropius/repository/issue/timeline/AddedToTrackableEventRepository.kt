package gropius.repository.issue.timeline

import gropius.model.issue.timeline.AddedToTrackableEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AddedToTrackableEvent]
 */
@Repository
interface AddedToTrackableEventRepository : GropiusRepository<AddedToTrackableEvent, String>