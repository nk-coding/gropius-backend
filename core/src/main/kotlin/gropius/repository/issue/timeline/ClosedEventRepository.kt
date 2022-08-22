package gropius.repository.issue.timeline

import gropius.model.issue.timeline.ClosedEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ClosedEvent]
 */
@Repository
interface ClosedEventRepository : GropiusRepository<ClosedEvent, String>