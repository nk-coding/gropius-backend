package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedArtefactEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedArtefactEvent]
 */
@Repository
interface RemovedArtefactEventRepository : ReactiveNeo4jRepository<RemovedArtefactEvent, String>