package gropius.repository.issue.timeline

import gropius.model.issue.timeline.Body
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Body]
 */
@Repository
interface BodyRepository : GropiusRepository<Body, String>