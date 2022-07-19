package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.Assignment

/**
 * Repository for [Assignment]
 */
@Repository
interface AssignmentRepository : ReactiveNeo4jRepository<Assignment, String>