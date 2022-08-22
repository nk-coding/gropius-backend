package gropius.repository.issue.timeline

import gropius.model.issue.timeline.DueDateChangedEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [DueDateChangedEvent]
 */
@Repository
interface DueDateChangedEventRepository : GropiusRepository<DueDateChangedEvent, String>