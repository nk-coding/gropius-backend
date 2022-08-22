package gropius.repository.issue.timeline

import gropius.model.issue.timeline.PriorityChangedEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [PriorityChangedEvent]
 */
@Repository
interface PriorityChangedEventRepository : GropiusRepository<PriorityChangedEvent, String>