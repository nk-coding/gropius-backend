package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.RemovedArtefactEvent

/**
 * Repository for [RemovedArtefactEvent]
 */
@Repository
interface RemovedArtefactEventRepository : ReactiveNeo4jRepository<RemovedArtefactEvent, String>