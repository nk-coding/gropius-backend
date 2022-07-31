package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedIncomingRelationEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedIncomingRelationEvent]
 */
@Repository
interface RemovedIncomingRelationEventRepository : ReactiveNeo4jRepository<RemovedIncomingRelationEvent, String>