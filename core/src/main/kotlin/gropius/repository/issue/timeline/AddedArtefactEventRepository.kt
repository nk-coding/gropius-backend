package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.AddedArtefactEvent

/**
 * Repository for [AddedArtefactEvent]
 */
@Repository
interface AddedArtefactEventRepository : ReactiveNeo4jRepository<AddedArtefactEvent, String>