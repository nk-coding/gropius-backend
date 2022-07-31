package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedLabelEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedLabelEvent]
 */
@Repository
interface RemovedLabelEventRepository : ReactiveNeo4jRepository<RemovedLabelEvent, String>