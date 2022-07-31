package gropius.repository.issue.timeline

import gropius.model.issue.timeline.AddedToPinnedIssuesEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AddedToPinnedIssuesEvent]
 */
@Repository
interface AddedToPinnedIssuesEventRepository : ReactiveNeo4jRepository<AddedToPinnedIssuesEvent, String>