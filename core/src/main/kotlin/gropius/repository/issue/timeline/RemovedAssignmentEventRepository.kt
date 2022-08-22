package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedAssignmentEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedAssignmentEvent]
 */
@Repository
interface RemovedAssignmentEventRepository : GropiusRepository<RemovedAssignmentEvent, String>