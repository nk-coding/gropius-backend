package gropius.repository.template

import gropius.model.template.AssignmentType
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AssignmentType]
 */
@Repository
interface AssignmentTypeRepository : GropiusRepository<AssignmentType, String>