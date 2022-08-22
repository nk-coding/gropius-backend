package gropius.repository.issue.timeline

import gropius.model.issue.timeline.IssueComment
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IssueComment]
 */
@Repository
interface IssueCommentRepository : GropiusRepository<IssueComment, String>