package gropius.repository.issue.timeline

import gropius.model.issue.timeline.AddedAffectedEntityEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AddedAffectedEntityEvent]
 */
@Repository
interface AddedAffectedEntityEventRepository : GropiusRepository<AddedAffectedEntityEvent, String>