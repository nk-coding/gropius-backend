package gropius.repository.issue.timeline

import gropius.model.issue.timeline.EstimatedTimeChangedEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [EstimatedTimeChangedEvent]
 */
@Repository
interface EstimatedTimeChangedEventRepository : GropiusRepository<EstimatedTimeChangedEvent, String>