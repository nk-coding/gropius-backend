package gropius.repository.issue.timeline

import gropius.model.issue.timeline.ReopenedEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ReopenedEvent]
 */
@Repository
interface ReopenedEventRepository : GropiusRepository<ReopenedEvent, String>