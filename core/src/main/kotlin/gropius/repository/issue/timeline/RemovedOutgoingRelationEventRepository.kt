package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedOutgoingRelationEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedOutgoingRelationEvent]
 */
@Repository
interface RemovedOutgoingRelationEventRepository : GropiusRepository<RemovedOutgoingRelationEvent, String>