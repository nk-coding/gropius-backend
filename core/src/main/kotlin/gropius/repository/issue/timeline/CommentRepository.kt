package gropius.repository.issue.timeline

import gropius.model.issue.timeline.Comment
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Comment]
 */
@Repository
interface CommentRepository : GropiusRepository<Comment, String>