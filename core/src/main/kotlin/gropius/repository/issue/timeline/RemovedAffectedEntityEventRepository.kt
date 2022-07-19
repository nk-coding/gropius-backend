package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.RemovedAffectedEntityEvent

/**
 * Repository for [RemovedAffectedEntityEvent]
 */
@Repository
interface RemovedAffectedEntityEventRepository : ReactiveNeo4jRepository<RemovedAffectedEntityEvent, String>