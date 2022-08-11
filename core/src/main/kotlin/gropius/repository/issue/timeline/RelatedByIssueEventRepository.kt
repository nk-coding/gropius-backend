package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RelatedByIssueEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RelatedByIssueEvent]
 */
@Repository
interface RelatedByIssueEventRepository : ReactiveNeo4jRepository<RelatedByIssueEvent, String>