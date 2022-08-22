package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedArtefactEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedArtefactEvent]
 */
@Repository
interface RemovedArtefactEventRepository : GropiusRepository<RemovedArtefactEvent, String>