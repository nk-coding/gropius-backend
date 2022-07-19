package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.Comment

/**
 * Repository for [Comment]
 */
@Repository
interface CommentRepository : ReactiveNeo4jRepository<Comment, String>