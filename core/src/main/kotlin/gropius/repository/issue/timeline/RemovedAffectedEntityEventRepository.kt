package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedAffectedEntityEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedAffectedEntityEvent]
 */
@Repository
interface RemovedAffectedEntityEventRepository : ReactiveNeo4jRepository<RemovedAffectedEntityEvent, String>