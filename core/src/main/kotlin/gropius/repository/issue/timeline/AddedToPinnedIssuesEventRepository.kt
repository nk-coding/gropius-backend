package gropius.repository.issue.timeline

import gropius.model.issue.timeline.AddedToPinnedIssuesEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AddedToPinnedIssuesEvent]
 */
@Repository
interface AddedToPinnedIssuesEventRepository : GropiusRepository<AddedToPinnedIssuesEvent, String>