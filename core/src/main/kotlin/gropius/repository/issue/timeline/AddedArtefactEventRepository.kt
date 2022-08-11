package gropius.repository.issue.timeline

import gropius.model.issue.timeline.AddedArtefactEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AddedArtefactEvent]
 */
@Repository
interface AddedArtefactEventRepository : ReactiveNeo4jRepository<AddedArtefactEvent, String>