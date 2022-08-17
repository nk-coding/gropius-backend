package gropius.repository.issue.timeline

import gropius.model.issue.timeline.Comment
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Comment]
 */
@Repository
interface CommentRepository : ReactiveNeo4jRepository<Comment, String>