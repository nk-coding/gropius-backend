package gropius.repository.template

import gropius.model.template.AssignmentType
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AssignmentType]
 */
@Repository
interface AssignmentTypeRepository : ReactiveNeo4jRepository<AssignmentType, String>