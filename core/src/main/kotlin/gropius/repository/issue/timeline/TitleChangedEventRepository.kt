package gropius.repository.issue.timeline

import gropius.model.issue.timeline.TitleChangedEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [TitleChangedEvent]
 */
@Repository
interface TitleChangedEventRepository : GropiusRepository<TitleChangedEvent, String>