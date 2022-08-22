package gropius.repository.template

import gropius.model.template.IssuePriority
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IssuePriority]
 */
@Repository
interface IssuePriorityRepository : GropiusRepository<IssuePriority, String>