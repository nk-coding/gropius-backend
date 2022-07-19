package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.RemovedIncomingRelationEvent

/**
 * Repository for [RemovedIncomingRelationEvent]
 */
@Repository
interface RemovedIncomingRelationEventRepository : ReactiveNeo4jRepository<RemovedIncomingRelationEvent, String>