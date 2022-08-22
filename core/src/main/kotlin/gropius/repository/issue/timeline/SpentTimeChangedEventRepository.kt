package gropius.repository.issue.timeline

import gropius.model.issue.timeline.SpentTimeChangedEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [SpentTimeChangedEvent]
 */
@Repository
interface SpentTimeChangedEventRepository : GropiusRepository<SpentTimeChangedEvent, String>