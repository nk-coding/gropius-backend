package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.RemovedAssignmentEvent

/**
 * Repository for [RemovedAssignmentEvent]
 */
@Repository
interface RemovedAssignmentEventRepository : ReactiveNeo4jRepository<RemovedAssignmentEvent, String>