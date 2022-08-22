package gropius.repository.issue.timeline

import gropius.model.issue.timeline.Assignment
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Assignment]
 */
@Repository
interface AssignmentRepository : GropiusRepository<Assignment, String>