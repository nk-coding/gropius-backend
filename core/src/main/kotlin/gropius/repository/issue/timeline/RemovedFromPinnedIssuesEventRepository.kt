package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RemovedFromPinnedIssuesEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RemovedFromPinnedIssuesEvent]
 */
@Repository
interface RemovedFromPinnedIssuesEventRepository : ReactiveNeo4jRepository<RemovedFromPinnedIssuesEvent, String>