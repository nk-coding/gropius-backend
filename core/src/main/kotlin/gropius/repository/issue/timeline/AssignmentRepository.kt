package gropius.repository.issue.timeline

import gropius.model.issue.timeline.Assignment
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Assignment]
 */
@Repository
interface AssignmentRepository : ReactiveNeo4jRepository<Assignment, String>