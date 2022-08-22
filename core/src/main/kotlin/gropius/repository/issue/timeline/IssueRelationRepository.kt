package gropius.repository.issue.timeline

import gropius.model.issue.timeline.IssueRelation
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IssueRelation]
 */
@Repository
interface IssueRelationRepository : GropiusRepository<IssueRelation, String>