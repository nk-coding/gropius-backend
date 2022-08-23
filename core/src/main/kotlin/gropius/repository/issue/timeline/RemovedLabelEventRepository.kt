package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedLabelEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedLabelEvent]
 */
@Repository
interface RemovedLabelEventRepository : GropiusRepository<RemovedLabelEvent, String>