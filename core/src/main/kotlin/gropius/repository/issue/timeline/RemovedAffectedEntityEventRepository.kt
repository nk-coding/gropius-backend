package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedAffectedEntityEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedAffectedEntityEvent]
 */
@Repository
interface RemovedAffectedEntityEventRepository : GropiusRepository<RemovedAffectedEntityEvent, String>