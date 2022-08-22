package gropius.repository.issue.timeline

import gropius.model.issue.timeline.AddedLabelEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AddedLabelEvent]
 */
@Repository
interface AddedLabelEventRepository : GropiusRepository<AddedLabelEvent, String>