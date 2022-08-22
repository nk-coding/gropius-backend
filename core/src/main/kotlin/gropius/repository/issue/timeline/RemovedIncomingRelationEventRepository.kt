package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedIncomingRelationEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedIncomingRelationEvent]
 */
@Repository
interface RemovedIncomingRelationEventRepository : GropiusRepository<RemovedIncomingRelationEvent, String>