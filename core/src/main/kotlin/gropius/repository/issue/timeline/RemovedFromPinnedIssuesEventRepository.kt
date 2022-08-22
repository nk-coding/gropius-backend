package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedFromPinnedIssuesEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedFromPinnedIssuesEvent]
 */
@Repository
interface RemovedFromPinnedIssuesEventRepository : GropiusRepository<RemovedFromPinnedIssuesEvent, String>