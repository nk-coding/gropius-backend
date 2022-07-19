package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.RemovedLabelEvent

/**
 * Repository for [RemovedLabelEvent]
 */
@Repository
interface RemovedLabelEventRepository : ReactiveNeo4jRepository<RemovedLabelEvent, String>