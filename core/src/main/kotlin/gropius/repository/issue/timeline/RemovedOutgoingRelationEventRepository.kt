package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.RemovedOutgoingRelationEvent

/**
 * Repository for [RemovedOutgoingRelationEvent]
 */
@Repository
interface RemovedOutgoingRelationEventRepository : ReactiveNeo4jRepository<RemovedOutgoingRelationEvent, String>