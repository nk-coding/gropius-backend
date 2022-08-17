package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedOutgoingRelationEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedOutgoingRelationEvent]
 */
@Repository
interface RemovedOutgoingRelationEventRepository : ReactiveNeo4jRepository<RemovedOutgoingRelationEvent, String>