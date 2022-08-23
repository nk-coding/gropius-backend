package gropius.repository.issue.timeline

import gropius.model.issue.timeline.AddedArtefactEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AddedArtefactEvent]
 */
@Repository
interface AddedArtefactEventRepository : GropiusRepository<AddedArtefactEvent, String>