package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.RemovedFromPinnedIssuesEvent

/**
 * Repository for [RemovedFromPinnedIssuesEvent]
 */
@Repository
interface RemovedFromPinnedIssuesEventRepository : ReactiveNeo4jRepository<RemovedFromPinnedIssuesEvent, String>