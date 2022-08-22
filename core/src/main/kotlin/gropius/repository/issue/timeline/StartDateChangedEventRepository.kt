package gropius.repository.issue.timeline

import gropius.model.issue.timeline.StartDateChangedEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [StartDateChangedEvent]
 */
@Repository
interface StartDateChangedEventRepository : GropiusRepository<StartDateChangedEvent, String>