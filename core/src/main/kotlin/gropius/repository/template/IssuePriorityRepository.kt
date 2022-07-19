package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.IssuePriority

/**
 * Repository for [IssuePriority]
 */
@Repository
interface IssuePriorityRepository : ReactiveNeo4jRepository<IssuePriority, String>