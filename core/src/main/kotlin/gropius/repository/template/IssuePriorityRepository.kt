package gropius.repository.template

import gropius.model.template.IssuePriority
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IssuePriority]
 */
@Repository
interface IssuePriorityRepository : ReactiveNeo4jRepository<IssuePriority, String>