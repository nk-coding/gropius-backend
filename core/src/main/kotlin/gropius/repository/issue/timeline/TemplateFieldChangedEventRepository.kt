package gropius.repository.issue.timeline

import gropius.model.issue.timeline.TemplateFieldChangedEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [TemplateFieldChangedEvent]
 */
@Repository
interface TemplateFieldChangedEventRepository : GropiusRepository<TemplateFieldChangedEvent, String>