package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.RelatedByIssueEvent

/**
 * Repository for [RelatedByIssueEvent]
 */
@Repository
interface RelatedByIssueEventRepository : ReactiveNeo4jRepository<RelatedByIssueEvent, String>