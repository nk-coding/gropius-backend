package gropius.repository.issue.timeline

import gropius.model.issue.timeline.RelatedByIssueEvent
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RelatedByIssueEvent]
 */
@Repository
interface RelatedByIssueEventRepository : GropiusRepository<RelatedByIssueEvent, String>