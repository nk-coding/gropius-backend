package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.AddedToPinnedIssuesEvent

/**
 * Repository for [AddedToPinnedIssuesEvent]
 */
@Repository
interface AddedToPinnedIssuesEventRepository : ReactiveNeo4jRepository<AddedToPinnedIssuesEvent, String>