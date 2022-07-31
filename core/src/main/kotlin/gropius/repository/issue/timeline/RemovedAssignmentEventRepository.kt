package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedAssignmentEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedAssignmentEvent]
 */
@Repository
interface RemovedAssignmentEventRepository : ReactiveNeo4jRepository<RemovedAssignmentEvent, String>