package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.AssignmentType

/**
 * Repository for [AssignmentType]
 */
@Repository
interface AssignmentTypeRepository : ReactiveNeo4jRepository<AssignmentType, String>