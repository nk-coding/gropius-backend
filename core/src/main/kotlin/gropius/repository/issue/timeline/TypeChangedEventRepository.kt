package gropius.repository.issue.timeline

import gropius.model.issue.timeline.TypeChangedEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [TypeChangedEvent]
 */
@Repository
interface TypeChangedEventRepository : GropiusRepository<TypeChangedEvent, String>